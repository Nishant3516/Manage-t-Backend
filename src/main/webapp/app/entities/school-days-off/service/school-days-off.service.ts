import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchoolDaysOff, getSchoolDaysOffIdentifier } from '../school-days-off.model';

export type EntityResponseType = HttpResponse<ISchoolDaysOff>;
export type EntityArrayResponseType = HttpResponse<ISchoolDaysOff[]>;

@Injectable({ providedIn: 'root' })
export class SchoolDaysOffService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/school-days-offs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(schoolDaysOff: ISchoolDaysOff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolDaysOff);
    return this.http
      .post<ISchoolDaysOff>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(schoolDaysOff: ISchoolDaysOff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolDaysOff);
    return this.http
      .put<ISchoolDaysOff>(`${this.resourceUrl}/${getSchoolDaysOffIdentifier(schoolDaysOff) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(schoolDaysOff: ISchoolDaysOff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolDaysOff);
    return this.http
      .patch<ISchoolDaysOff>(`${this.resourceUrl}/${getSchoolDaysOffIdentifier(schoolDaysOff) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISchoolDaysOff>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISchoolDaysOff[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSchoolDaysOffToCollectionIfMissing(
    schoolDaysOffCollection: ISchoolDaysOff[],
    ...schoolDaysOffsToCheck: (ISchoolDaysOff | null | undefined)[]
  ): ISchoolDaysOff[] {
    const schoolDaysOffs: ISchoolDaysOff[] = schoolDaysOffsToCheck.filter(isPresent);
    if (schoolDaysOffs.length > 0) {
      const schoolDaysOffCollectionIdentifiers = schoolDaysOffCollection.map(
        schoolDaysOffItem => getSchoolDaysOffIdentifier(schoolDaysOffItem)!
      );
      const schoolDaysOffsToAdd = schoolDaysOffs.filter(schoolDaysOffItem => {
        const schoolDaysOffIdentifier = getSchoolDaysOffIdentifier(schoolDaysOffItem);
        if (schoolDaysOffIdentifier == null || schoolDaysOffCollectionIdentifiers.includes(schoolDaysOffIdentifier)) {
          return false;
        }
        schoolDaysOffCollectionIdentifiers.push(schoolDaysOffIdentifier);
        return true;
      });
      return [...schoolDaysOffsToAdd, ...schoolDaysOffCollection];
    }
    return schoolDaysOffCollection;
  }

  protected convertDateFromClient(schoolDaysOff: ISchoolDaysOff): ISchoolDaysOff {
    return Object.assign({}, schoolDaysOff, {
      startDate: schoolDaysOff.startDate?.isValid() ? schoolDaysOff.startDate.format(DATE_FORMAT) : undefined,
      endDate: schoolDaysOff.endDate?.isValid() ? schoolDaysOff.endDate.format(DATE_FORMAT) : undefined,
      createDate: schoolDaysOff.createDate?.isValid() ? schoolDaysOff.createDate.format(DATE_FORMAT) : undefined,
      lastModified: schoolDaysOff.lastModified?.isValid() ? schoolDaysOff.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: schoolDaysOff.cancelDate?.isValid() ? schoolDaysOff.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((schoolDaysOff: ISchoolDaysOff) => {
        schoolDaysOff.startDate = schoolDaysOff.startDate ? dayjs(schoolDaysOff.startDate) : undefined;
        schoolDaysOff.endDate = schoolDaysOff.endDate ? dayjs(schoolDaysOff.endDate) : undefined;
        schoolDaysOff.createDate = schoolDaysOff.createDate ? dayjs(schoolDaysOff.createDate) : undefined;
        schoolDaysOff.lastModified = schoolDaysOff.lastModified ? dayjs(schoolDaysOff.lastModified) : undefined;
        schoolDaysOff.cancelDate = schoolDaysOff.cancelDate ? dayjs(schoolDaysOff.cancelDate) : undefined;
      });
    }
    return res;
  }
}
