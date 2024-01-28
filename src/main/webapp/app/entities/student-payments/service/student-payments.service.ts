import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudentPayments, getStudentPaymentsIdentifier } from '../student-payments.model';

export type EntityResponseType = HttpResponse<IStudentPayments>;
export type EntityArrayResponseType = HttpResponse<IStudentPayments[]>;

@Injectable({ providedIn: 'root' })
export class StudentPaymentsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/student-payments');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(studentPayments: IStudentPayments): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentPayments);
    return this.http
      .post<IStudentPayments>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(studentPayments: IStudentPayments): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentPayments);
    return this.http
      .put<IStudentPayments>(`${this.resourceUrl}/${getStudentPaymentsIdentifier(studentPayments) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(studentPayments: IStudentPayments): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentPayments);
    return this.http
      .patch<IStudentPayments>(`${this.resourceUrl}/${getStudentPaymentsIdentifier(studentPayments) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStudentPayments>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStudentPayments[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStudentPaymentsToCollectionIfMissing(
    studentPaymentsCollection: IStudentPayments[],
    ...studentPaymentsToCheck: (IStudentPayments | null | undefined)[]
  ): IStudentPayments[] {
    const studentPayments: IStudentPayments[] = studentPaymentsToCheck.filter(isPresent);
    if (studentPayments.length > 0) {
      const studentPaymentsCollectionIdentifiers = studentPaymentsCollection.map(
        studentPaymentsItem => getStudentPaymentsIdentifier(studentPaymentsItem)!
      );
      const studentPaymentsToAdd = studentPayments.filter(studentPaymentsItem => {
        const studentPaymentsIdentifier = getStudentPaymentsIdentifier(studentPaymentsItem);
        if (studentPaymentsIdentifier == null || studentPaymentsCollectionIdentifiers.includes(studentPaymentsIdentifier)) {
          return false;
        }
        studentPaymentsCollectionIdentifiers.push(studentPaymentsIdentifier);
        return true;
      });
      return [...studentPaymentsToAdd, ...studentPaymentsCollection];
    }
    return studentPaymentsCollection;
  }

  protected convertDateFromClient(studentPayments: IStudentPayments): IStudentPayments {
    return Object.assign({}, studentPayments, {
      paymentDate: studentPayments.paymentDate?.isValid() ? studentPayments.paymentDate.format(DATE_FORMAT) : undefined,
      createDate: studentPayments.createDate?.isValid() ? studentPayments.createDate.format(DATE_FORMAT) : undefined,
      lastModified: studentPayments.lastModified?.isValid() ? studentPayments.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: studentPayments.cancelDate?.isValid() ? studentPayments.cancelDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.paymentDate = res.body.paymentDate ? dayjs(res.body.paymentDate) : undefined;
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
      res.body.cancelDate = res.body.cancelDate ? dayjs(res.body.cancelDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((studentPayments: IStudentPayments) => {
        studentPayments.paymentDate = studentPayments.paymentDate ? dayjs(studentPayments.paymentDate) : undefined;
        studentPayments.createDate = studentPayments.createDate ? dayjs(studentPayments.createDate) : undefined;
        studentPayments.lastModified = studentPayments.lastModified ? dayjs(studentPayments.lastModified) : undefined;
        studentPayments.cancelDate = studentPayments.cancelDate ? dayjs(studentPayments.cancelDate) : undefined;
      });
    }
    return res;
  }
}
