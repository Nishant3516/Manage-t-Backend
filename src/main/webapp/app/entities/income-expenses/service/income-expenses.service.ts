import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIncomeExpenses, getIncomeExpensesIdentifier } from '../income-expenses.model';

export type EntityResponseType = HttpResponse<IIncomeExpenses>;
export type EntityArrayResponseType = HttpResponse<IIncomeExpenses[]>;

@Injectable({ providedIn: 'root' })
export class IncomeExpensesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/income-expenses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(incomeExpenses: IIncomeExpenses): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incomeExpenses);
    return this.http
      .post<IIncomeExpenses>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(incomeExpenses: IIncomeExpenses): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incomeExpenses);
    return this.http
      .put<IIncomeExpenses>(`${this.resourceUrl}/${getIncomeExpensesIdentifier(incomeExpenses) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(incomeExpenses: IIncomeExpenses): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incomeExpenses);
    return this.http
      .patch<IIncomeExpenses>(`${this.resourceUrl}/${getIncomeExpensesIdentifier(incomeExpenses) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIncomeExpenses>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIncomeExpenses[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIncomeExpensesToCollectionIfMissing(
    incomeExpensesCollection: IIncomeExpenses[],
    ...incomeExpensesToCheck: (IIncomeExpenses | null | undefined)[]
  ): IIncomeExpenses[] {
    const incomeExpenses: IIncomeExpenses[] = incomeExpensesToCheck.filter(isPresent);
    if (incomeExpenses.length > 0) {
      const incomeExpensesCollectionIdentifiers = incomeExpensesCollection.map(
        incomeExpensesItem => getIncomeExpensesIdentifier(incomeExpensesItem)!
      );
      const incomeExpensesToAdd = incomeExpenses.filter(incomeExpensesItem => {
        const incomeExpensesIdentifier = getIncomeExpensesIdentifier(incomeExpensesItem);
        if (incomeExpensesIdentifier == null || incomeExpensesCollectionIdentifiers.includes(incomeExpensesIdentifier)) {
          return false;
        }
        incomeExpensesCollectionIdentifiers.push(incomeExpensesIdentifier);
        return true;
      });
      return [...incomeExpensesToAdd, ...incomeExpensesCollection];
    }
    return incomeExpensesCollection;
  }

  protected convertDateFromClient(incomeExpenses: IIncomeExpenses): IIncomeExpenses {
    return Object.assign({}, incomeExpenses, {
      paymentDate: incomeExpenses.paymentDate?.isValid() ? incomeExpenses.paymentDate.format(DATE_FORMAT) : undefined,
      createDate: incomeExpenses.createDate?.isValid() ? incomeExpenses.createDate.format(DATE_FORMAT) : undefined,
      lastModified: incomeExpenses.lastModified?.isValid() ? incomeExpenses.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: incomeExpenses.cancelDate?.isValid() ? incomeExpenses.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((incomeExpenses: IIncomeExpenses) => {
        incomeExpenses.paymentDate = incomeExpenses.paymentDate ? dayjs(incomeExpenses.paymentDate) : undefined;
        incomeExpenses.createDate = incomeExpenses.createDate ? dayjs(incomeExpenses.createDate) : undefined;
        incomeExpenses.lastModified = incomeExpenses.lastModified ? dayjs(incomeExpenses.lastModified) : undefined;
        incomeExpenses.cancelDate = incomeExpenses.cancelDate ? dayjs(incomeExpenses.cancelDate) : undefined;
      });
    }
    return res;
  }
}
