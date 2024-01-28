import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchoolLedgerHead, getSchoolLedgerHeadIdentifier } from '../school-ledger-head.model';

export type EntityResponseType = HttpResponse<ISchoolLedgerHead>;
export type EntityArrayResponseType = HttpResponse<ISchoolLedgerHead[]>;

@Injectable({ providedIn: 'root' })
export class SchoolLedgerHeadService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/school-ledger-heads');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(schoolLedgerHead: ISchoolLedgerHead): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolLedgerHead);
    return this.http
      .post<ISchoolLedgerHead>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(schoolLedgerHead: ISchoolLedgerHead): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolLedgerHead);
    return this.http
      .put<ISchoolLedgerHead>(`${this.resourceUrl}/${getSchoolLedgerHeadIdentifier(schoolLedgerHead) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(schoolLedgerHead: ISchoolLedgerHead): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolLedgerHead);
    return this.http
      .patch<ISchoolLedgerHead>(`${this.resourceUrl}/${getSchoolLedgerHeadIdentifier(schoolLedgerHead) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISchoolLedgerHead>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISchoolLedgerHead[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSchoolLedgerHeadToCollectionIfMissing(
    schoolLedgerHeadCollection: ISchoolLedgerHead[],
    ...schoolLedgerHeadsToCheck: (ISchoolLedgerHead | null | undefined)[]
  ): ISchoolLedgerHead[] {
    const schoolLedgerHeads: ISchoolLedgerHead[] = schoolLedgerHeadsToCheck.filter(isPresent);
    if (schoolLedgerHeads.length > 0) {
      const schoolLedgerHeadCollectionIdentifiers = schoolLedgerHeadCollection.map(
        schoolLedgerHeadItem => getSchoolLedgerHeadIdentifier(schoolLedgerHeadItem)!
      );
      const schoolLedgerHeadsToAdd = schoolLedgerHeads.filter(schoolLedgerHeadItem => {
        const schoolLedgerHeadIdentifier = getSchoolLedgerHeadIdentifier(schoolLedgerHeadItem);
        if (schoolLedgerHeadIdentifier == null || schoolLedgerHeadCollectionIdentifiers.includes(schoolLedgerHeadIdentifier)) {
          return false;
        }
        schoolLedgerHeadCollectionIdentifiers.push(schoolLedgerHeadIdentifier);
        return true;
      });
      return [...schoolLedgerHeadsToAdd, ...schoolLedgerHeadCollection];
    }
    return schoolLedgerHeadCollection;
  }

  protected convertDateFromClient(schoolLedgerHead: ISchoolLedgerHead): ISchoolLedgerHead {
    return Object.assign({}, schoolLedgerHead, {
      createDate: schoolLedgerHead.createDate?.isValid() ? schoolLedgerHead.createDate.format(DATE_FORMAT) : undefined,
      lastModified: schoolLedgerHead.lastModified?.isValid() ? schoolLedgerHead.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: schoolLedgerHead.cancelDate?.isValid() ? schoolLedgerHead.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((schoolLedgerHead: ISchoolLedgerHead) => {
        schoolLedgerHead.createDate = schoolLedgerHead.createDate ? dayjs(schoolLedgerHead.createDate) : undefined;
        schoolLedgerHead.lastModified = schoolLedgerHead.lastModified ? dayjs(schoolLedgerHead.lastModified) : undefined;
        schoolLedgerHead.cancelDate = schoolLedgerHead.cancelDate ? dayjs(schoolLedgerHead.cancelDate) : undefined;
      });
    }
    return res;
  }
}
