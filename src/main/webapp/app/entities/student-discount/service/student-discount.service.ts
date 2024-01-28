import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudentDiscount, getStudentDiscountIdentifier } from '../student-discount.model';

export type EntityResponseType = HttpResponse<IStudentDiscount>;
export type EntityArrayResponseType = HttpResponse<IStudentDiscount[]>;

@Injectable({ providedIn: 'root' })
export class StudentDiscountService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/student-discounts');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(studentDiscount: IStudentDiscount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentDiscount);
    return this.http
      .post<IStudentDiscount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(studentDiscount: IStudentDiscount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentDiscount);
    return this.http
      .put<IStudentDiscount>(`${this.resourceUrl}/${getStudentDiscountIdentifier(studentDiscount) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(studentDiscount: IStudentDiscount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentDiscount);
    return this.http
      .patch<IStudentDiscount>(`${this.resourceUrl}/${getStudentDiscountIdentifier(studentDiscount) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStudentDiscount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStudentDiscount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStudentDiscountToCollectionIfMissing(
    studentDiscountCollection: IStudentDiscount[],
    ...studentDiscountsToCheck: (IStudentDiscount | null | undefined)[]
  ): IStudentDiscount[] {
    const studentDiscounts: IStudentDiscount[] = studentDiscountsToCheck.filter(isPresent);
    if (studentDiscounts.length > 0) {
      const studentDiscountCollectionIdentifiers = studentDiscountCollection.map(
        studentDiscountItem => getStudentDiscountIdentifier(studentDiscountItem)!
      );
      const studentDiscountsToAdd = studentDiscounts.filter(studentDiscountItem => {
        const studentDiscountIdentifier = getStudentDiscountIdentifier(studentDiscountItem);
        if (studentDiscountIdentifier == null || studentDiscountCollectionIdentifiers.includes(studentDiscountIdentifier)) {
          return false;
        }
        studentDiscountCollectionIdentifiers.push(studentDiscountIdentifier);
        return true;
      });
      return [...studentDiscountsToAdd, ...studentDiscountCollection];
    }
    return studentDiscountCollection;
  }

  protected convertDateFromClient(studentDiscount: IStudentDiscount): IStudentDiscount {
    return Object.assign({}, studentDiscount, {
      createDate: studentDiscount.createDate?.isValid() ? studentDiscount.createDate.format(DATE_FORMAT) : undefined,
      lastModified: studentDiscount.lastModified?.isValid() ? studentDiscount.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: studentDiscount.cancelDate?.isValid() ? studentDiscount.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((studentDiscount: IStudentDiscount) => {
        studentDiscount.createDate = studentDiscount.createDate ? dayjs(studentDiscount.createDate) : undefined;
        studentDiscount.lastModified = studentDiscount.lastModified ? dayjs(studentDiscount.lastModified) : undefined;
        studentDiscount.cancelDate = studentDiscount.cancelDate ? dayjs(studentDiscount.cancelDate) : undefined;
      });
    }
    return res;
  }
}
