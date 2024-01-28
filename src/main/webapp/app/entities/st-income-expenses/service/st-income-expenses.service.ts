import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISTIncomeExpenses, getSTIncomeExpensesIdentifier } from '../st-income-expenses.model';

export type EntityResponseType = HttpResponse<ISTIncomeExpenses>;
export type EntityArrayResponseType = HttpResponse<ISTIncomeExpenses[]>;

@Injectable({ providedIn: 'root' })
export class STIncomeExpensesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/st-income-expenses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sTIncomeExpenses: ISTIncomeExpenses): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sTIncomeExpenses);
    return this.http
      .post<ISTIncomeExpenses>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sTIncomeExpenses: ISTIncomeExpenses): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sTIncomeExpenses);
    return this.http
      .put<ISTIncomeExpenses>(`${this.resourceUrl}/${getSTIncomeExpensesIdentifier(sTIncomeExpenses) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(sTIncomeExpenses: ISTIncomeExpenses): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sTIncomeExpenses);
    return this.http
      .patch<ISTIncomeExpenses>(`${this.resourceUrl}/${getSTIncomeExpensesIdentifier(sTIncomeExpenses) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISTIncomeExpenses>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISTIncomeExpenses[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSTIncomeExpensesToCollectionIfMissing(
    sTIncomeExpensesCollection: ISTIncomeExpenses[],
    ...sTIncomeExpensesToCheck: (ISTIncomeExpenses | null | undefined)[]
  ): ISTIncomeExpenses[] {
    const sTIncomeExpenses: ISTIncomeExpenses[] = sTIncomeExpensesToCheck.filter(isPresent);
    if (sTIncomeExpenses.length > 0) {
      const sTIncomeExpensesCollectionIdentifiers = sTIncomeExpensesCollection.map(
        sTIncomeExpensesItem => getSTIncomeExpensesIdentifier(sTIncomeExpensesItem)!
      );
      const sTIncomeExpensesToAdd = sTIncomeExpenses.filter(sTIncomeExpensesItem => {
        const sTIncomeExpensesIdentifier = getSTIncomeExpensesIdentifier(sTIncomeExpensesItem);
        if (sTIncomeExpensesIdentifier == null || sTIncomeExpensesCollectionIdentifiers.includes(sTIncomeExpensesIdentifier)) {
          return false;
        }
        sTIncomeExpensesCollectionIdentifiers.push(sTIncomeExpensesIdentifier);
        return true;
      });
      return [...sTIncomeExpensesToAdd, ...sTIncomeExpensesCollection];
    }
    return sTIncomeExpensesCollection;
  }

  protected convertDateFromClient(sTIncomeExpenses: ISTIncomeExpenses): ISTIncomeExpenses {
    return Object.assign({}, sTIncomeExpenses, {
      paymentDate: sTIncomeExpenses.paymentDate?.isValid() ? sTIncomeExpenses.paymentDate.format(DATE_FORMAT) : undefined,
      createDate: sTIncomeExpenses.createDate?.isValid() ? sTIncomeExpenses.createDate.format(DATE_FORMAT) : undefined,
      lastModified: sTIncomeExpenses.lastModified?.isValid() ? sTIncomeExpenses.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: sTIncomeExpenses.cancelDate?.isValid() ? sTIncomeExpenses.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((sTIncomeExpenses: ISTIncomeExpenses) => {
        sTIncomeExpenses.paymentDate = sTIncomeExpenses.paymentDate ? dayjs(sTIncomeExpenses.paymentDate) : undefined;
        sTIncomeExpenses.createDate = sTIncomeExpenses.createDate ? dayjs(sTIncomeExpenses.createDate) : undefined;
        sTIncomeExpenses.lastModified = sTIncomeExpenses.lastModified ? dayjs(sTIncomeExpenses.lastModified) : undefined;
        sTIncomeExpenses.cancelDate = sTIncomeExpenses.cancelDate ? dayjs(sTIncomeExpenses.cancelDate) : undefined;
      });
    }
    return res;
  }
}
