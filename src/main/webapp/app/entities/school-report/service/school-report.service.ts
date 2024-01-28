import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchoolReport, getSchoolReportIdentifier } from '../school-report.model';

export type EntityResponseType = HttpResponse<ISchoolReport>;
export type EntityArrayResponseType = HttpResponse<ISchoolReport[]>;

@Injectable({ providedIn: 'root' })
export class SchoolReportService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/school-reports');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(schoolReport: ISchoolReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolReport);
    return this.http
      .post<ISchoolReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(schoolReport: ISchoolReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolReport);
    return this.http
      .put<ISchoolReport>(`${this.resourceUrl}/${getSchoolReportIdentifier(schoolReport) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(schoolReport: ISchoolReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolReport);
    return this.http
      .patch<ISchoolReport>(`${this.resourceUrl}/${getSchoolReportIdentifier(schoolReport) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISchoolReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISchoolReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSchoolReportToCollectionIfMissing(
    schoolReportCollection: ISchoolReport[],
    ...schoolReportsToCheck: (ISchoolReport | null | undefined)[]
  ): ISchoolReport[] {
    const schoolReports: ISchoolReport[] = schoolReportsToCheck.filter(isPresent);
    if (schoolReports.length > 0) {
      const schoolReportCollectionIdentifiers = schoolReportCollection.map(
        schoolReportItem => getSchoolReportIdentifier(schoolReportItem)!
      );
      const schoolReportsToAdd = schoolReports.filter(schoolReportItem => {
        const schoolReportIdentifier = getSchoolReportIdentifier(schoolReportItem);
        if (schoolReportIdentifier == null || schoolReportCollectionIdentifiers.includes(schoolReportIdentifier)) {
          return false;
        }
        schoolReportCollectionIdentifiers.push(schoolReportIdentifier);
        return true;
      });
      return [...schoolReportsToAdd, ...schoolReportCollection];
    }
    return schoolReportCollection;
  }

  protected convertDateFromClient(schoolReport: ISchoolReport): ISchoolReport {
    return Object.assign({}, schoolReport, {
      startDate: schoolReport.startDate?.isValid() ? schoolReport.startDate.format(DATE_FORMAT) : undefined,
      endDate: schoolReport.endDate?.isValid() ? schoolReport.endDate.format(DATE_FORMAT) : undefined,
      createDate: schoolReport.createDate?.isValid() ? schoolReport.createDate.format(DATE_FORMAT) : undefined,
      lastModified: schoolReport.lastModified?.isValid() ? schoolReport.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: schoolReport.cancelDate?.isValid() ? schoolReport.cancelDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
      res.body.cancelDate = res.body.cancelDate ? dayjs(res.body.cancelDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((schoolReport: ISchoolReport) => {
        schoolReport.startDate = schoolReport.startDate ? dayjs(schoolReport.startDate) : undefined;
        schoolReport.endDate = schoolReport.endDate ? dayjs(schoolReport.endDate) : undefined;
        schoolReport.createDate = schoolReport.createDate ? dayjs(schoolReport.createDate) : undefined;
        schoolReport.lastModified = schoolReport.lastModified ? dayjs(schoolReport.lastModified) : undefined;
        schoolReport.cancelDate = schoolReport.cancelDate ? dayjs(schoolReport.cancelDate) : undefined;
      });
    }
    return res;
  }
}
