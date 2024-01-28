import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudentChargesSummary, getStudentChargesSummaryIdentifier } from '../student-charges-summary.model';

export type EntityResponseType = HttpResponse<IStudentChargesSummary>;
export type EntityArrayResponseType = HttpResponse<IStudentChargesSummary[]>;

@Injectable({ providedIn: 'root' })
export class StudentChargesSummaryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/student-charges-summaries');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(studentChargesSummary: IStudentChargesSummary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentChargesSummary);
    return this.http
      .post<IStudentChargesSummary>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(studentChargesSummary: IStudentChargesSummary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentChargesSummary);
    return this.http
      .put<IStudentChargesSummary>(`${this.resourceUrl}/${getStudentChargesSummaryIdentifier(studentChargesSummary) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(studentChargesSummary: IStudentChargesSummary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentChargesSummary);
    return this.http
      .patch<IStudentChargesSummary>(`${this.resourceUrl}/${getStudentChargesSummaryIdentifier(studentChargesSummary) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStudentChargesSummary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStudentChargesSummary[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStudentChargesSummaryToCollectionIfMissing(
    studentChargesSummaryCollection: IStudentChargesSummary[],
    ...studentChargesSummariesToCheck: (IStudentChargesSummary | null | undefined)[]
  ): IStudentChargesSummary[] {
    const studentChargesSummaries: IStudentChargesSummary[] = studentChargesSummariesToCheck.filter(isPresent);
    if (studentChargesSummaries.length > 0) {
      const studentChargesSummaryCollectionIdentifiers = studentChargesSummaryCollection.map(
        studentChargesSummaryItem => getStudentChargesSummaryIdentifier(studentChargesSummaryItem)!
      );
      const studentChargesSummariesToAdd = studentChargesSummaries.filter(studentChargesSummaryItem => {
        const studentChargesSummaryIdentifier = getStudentChargesSummaryIdentifier(studentChargesSummaryItem);
        if (
          studentChargesSummaryIdentifier == null ||
          studentChargesSummaryCollectionIdentifiers.includes(studentChargesSummaryIdentifier)
        ) {
          return false;
        }
        studentChargesSummaryCollectionIdentifiers.push(studentChargesSummaryIdentifier);
        return true;
      });
      return [...studentChargesSummariesToAdd, ...studentChargesSummaryCollection];
    }
    return studentChargesSummaryCollection;
  }

  protected convertDateFromClient(studentChargesSummary: IStudentChargesSummary): IStudentChargesSummary {
    return Object.assign({}, studentChargesSummary, {
      createDate: studentChargesSummary.createDate?.isValid() ? studentChargesSummary.createDate.format(DATE_FORMAT) : undefined,
      lastModified: studentChargesSummary.lastModified?.isValid() ? studentChargesSummary.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: studentChargesSummary.cancelDate?.isValid() ? studentChargesSummary.cancelDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
      res.body.cancelDate = res.body.cancelDate ? dayjs(res.body.cancelDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((studentChargesSummary: IStudentChargesSummary) => {
        studentChargesSummary.createDate = studentChargesSummary.createDate ? dayjs(studentChargesSummary.createDate) : undefined;
        studentChargesSummary.lastModified = studentChargesSummary.lastModified ? dayjs(studentChargesSummary.lastModified) : undefined;
        studentChargesSummary.cancelDate = studentChargesSummary.cancelDate ? dayjs(studentChargesSummary.cancelDate) : undefined;
      });
    }
    return res;
  }
}
