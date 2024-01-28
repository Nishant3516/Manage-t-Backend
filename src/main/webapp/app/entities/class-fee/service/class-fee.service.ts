import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassFee, getClassFeeIdentifier } from '../class-fee.model';

export type EntityResponseType = HttpResponse<IClassFee>;
export type EntityArrayResponseType = HttpResponse<IClassFee[]>;

@Injectable({ providedIn: 'root' })
export class ClassFeeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/class-fees');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(classFee: IClassFee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classFee);
    return this.http
      .post<IClassFee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(classFee: IClassFee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classFee);
    return this.http
      .put<IClassFee>(`${this.resourceUrl}/${getClassFeeIdentifier(classFee) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(classFee: IClassFee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classFee);
    return this.http
      .patch<IClassFee>(`${this.resourceUrl}/${getClassFeeIdentifier(classFee) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IClassFee>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClassFee[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClassFeeToCollectionIfMissing(classFeeCollection: IClassFee[], ...classFeesToCheck: (IClassFee | null | undefined)[]): IClassFee[] {
    const classFees: IClassFee[] = classFeesToCheck.filter(isPresent);
    if (classFees.length > 0) {
      const classFeeCollectionIdentifiers = classFeeCollection.map(classFeeItem => getClassFeeIdentifier(classFeeItem)!);
      const classFeesToAdd = classFees.filter(classFeeItem => {
        const classFeeIdentifier = getClassFeeIdentifier(classFeeItem);
        if (classFeeIdentifier == null || classFeeCollectionIdentifiers.includes(classFeeIdentifier)) {
          return false;
        }
        classFeeCollectionIdentifiers.push(classFeeIdentifier);
        return true;
      });
      return [...classFeesToAdd, ...classFeeCollection];
    }
    return classFeeCollection;
  }

  protected convertDateFromClient(classFee: IClassFee): IClassFee {
    return Object.assign({}, classFee, {
      payByDate: classFee.payByDate?.isValid() ? classFee.payByDate.format(DATE_FORMAT) : undefined,
      createDate: classFee.createDate?.isValid() ? classFee.createDate.format(DATE_FORMAT) : undefined,
      lastModified: classFee.lastModified?.isValid() ? classFee.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: classFee.cancelDate?.isValid() ? classFee.cancelDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.payByDate = res.body.payByDate ? dayjs(res.body.payByDate) : undefined;
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
      res.body.cancelDate = res.body.cancelDate ? dayjs(res.body.cancelDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((classFee: IClassFee) => {
        classFee.payByDate = classFee.payByDate ? dayjs(classFee.payByDate) : undefined;
        classFee.createDate = classFee.createDate ? dayjs(classFee.createDate) : undefined;
        classFee.lastModified = classFee.lastModified ? dayjs(classFee.lastModified) : undefined;
        classFee.cancelDate = classFee.cancelDate ? dayjs(classFee.cancelDate) : undefined;
      });
    }
    return res;
  }
}
