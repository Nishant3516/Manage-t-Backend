import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudentAdditionalCharges, getStudentAdditionalChargesIdentifier } from '../student-additional-charges.model';

export type EntityResponseType = HttpResponse<IStudentAdditionalCharges>;
export type EntityArrayResponseType = HttpResponse<IStudentAdditionalCharges[]>;

@Injectable({ providedIn: 'root' })
export class StudentAdditionalChargesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/student-additional-charges');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(studentAdditionalCharges: IStudentAdditionalCharges): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentAdditionalCharges);
    return this.http
      .post<IStudentAdditionalCharges>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(studentAdditionalCharges: IStudentAdditionalCharges): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentAdditionalCharges);
    return this.http
      .put<IStudentAdditionalCharges>(
        `${this.resourceUrl}/${getStudentAdditionalChargesIdentifier(studentAdditionalCharges) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(studentAdditionalCharges: IStudentAdditionalCharges): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentAdditionalCharges);
    return this.http
      .patch<IStudentAdditionalCharges>(
        `${this.resourceUrl}/${getStudentAdditionalChargesIdentifier(studentAdditionalCharges) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStudentAdditionalCharges>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStudentAdditionalCharges[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStudentAdditionalChargesToCollectionIfMissing(
    studentAdditionalChargesCollection: IStudentAdditionalCharges[],
    ...studentAdditionalChargesToCheck: (IStudentAdditionalCharges | null | undefined)[]
  ): IStudentAdditionalCharges[] {
    const studentAdditionalCharges: IStudentAdditionalCharges[] = studentAdditionalChargesToCheck.filter(isPresent);
    if (studentAdditionalCharges.length > 0) {
      const studentAdditionalChargesCollectionIdentifiers = studentAdditionalChargesCollection.map(
        studentAdditionalChargesItem => getStudentAdditionalChargesIdentifier(studentAdditionalChargesItem)!
      );
      const studentAdditionalChargesToAdd = studentAdditionalCharges.filter(studentAdditionalChargesItem => {
        const studentAdditionalChargesIdentifier = getStudentAdditionalChargesIdentifier(studentAdditionalChargesItem);
        if (
          studentAdditionalChargesIdentifier == null ||
          studentAdditionalChargesCollectionIdentifiers.includes(studentAdditionalChargesIdentifier)
        ) {
          return false;
        }
        studentAdditionalChargesCollectionIdentifiers.push(studentAdditionalChargesIdentifier);
        return true;
      });
      return [...studentAdditionalChargesToAdd, ...studentAdditionalChargesCollection];
    }
    return studentAdditionalChargesCollection;
  }

  protected convertDateFromClient(studentAdditionalCharges: IStudentAdditionalCharges): IStudentAdditionalCharges {
    return Object.assign({}, studentAdditionalCharges, {
      createDate: studentAdditionalCharges.createDate?.isValid() ? studentAdditionalCharges.createDate.format(DATE_FORMAT) : undefined,
      lastModified: studentAdditionalCharges.lastModified?.isValid()
        ? studentAdditionalCharges.lastModified.format(DATE_FORMAT)
        : undefined,
      cancelDate: studentAdditionalCharges.cancelDate?.isValid() ? studentAdditionalCharges.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((studentAdditionalCharges: IStudentAdditionalCharges) => {
        studentAdditionalCharges.createDate = studentAdditionalCharges.createDate ? dayjs(studentAdditionalCharges.createDate) : undefined;
        studentAdditionalCharges.lastModified = studentAdditionalCharges.lastModified
          ? dayjs(studentAdditionalCharges.lastModified)
          : undefined;
        studentAdditionalCharges.cancelDate = studentAdditionalCharges.cancelDate ? dayjs(studentAdditionalCharges.cancelDate) : undefined;
      });
    }
    return res;
  }
}
