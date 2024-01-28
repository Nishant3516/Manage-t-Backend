import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVendors, getVendorsIdentifier } from '../vendors.model';

export type EntityResponseType = HttpResponse<IVendors>;
export type EntityArrayResponseType = HttpResponse<IVendors[]>;

@Injectable({ providedIn: 'root' })
export class VendorsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vendors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vendors: IVendors): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vendors);
    return this.http
      .post<IVendors>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vendors: IVendors): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vendors);
    return this.http
      .put<IVendors>(`${this.resourceUrl}/${getVendorsIdentifier(vendors) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vendors: IVendors): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vendors);
    return this.http
      .patch<IVendors>(`${this.resourceUrl}/${getVendorsIdentifier(vendors) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVendors>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVendors[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVendorsToCollectionIfMissing(vendorsCollection: IVendors[], ...vendorsToCheck: (IVendors | null | undefined)[]): IVendors[] {
    const vendors: IVendors[] = vendorsToCheck.filter(isPresent);
    if (vendors.length > 0) {
      const vendorsCollectionIdentifiers = vendorsCollection.map(vendorsItem => getVendorsIdentifier(vendorsItem)!);
      const vendorsToAdd = vendors.filter(vendorsItem => {
        const vendorsIdentifier = getVendorsIdentifier(vendorsItem);
        if (vendorsIdentifier == null || vendorsCollectionIdentifiers.includes(vendorsIdentifier)) {
          return false;
        }
        vendorsCollectionIdentifiers.push(vendorsIdentifier);
        return true;
      });
      return [...vendorsToAdd, ...vendorsCollection];
    }
    return vendorsCollection;
  }

  protected convertDateFromClient(vendors: IVendors): IVendors {
    return Object.assign({}, vendors, {
      dateOfBirth: vendors.dateOfBirth?.isValid() ? vendors.dateOfBirth.toJSON() : undefined,
      createDate: vendors.createDate?.isValid() ? vendors.createDate.format(DATE_FORMAT) : undefined,
      cancelDate: vendors.cancelDate?.isValid() ? vendors.cancelDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? dayjs(res.body.dateOfBirth) : undefined;
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.cancelDate = res.body.cancelDate ? dayjs(res.body.cancelDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vendors: IVendors) => {
        vendors.dateOfBirth = vendors.dateOfBirth ? dayjs(vendors.dateOfBirth) : undefined;
        vendors.createDate = vendors.createDate ? dayjs(vendors.createDate) : undefined;
        vendors.cancelDate = vendors.cancelDate ? dayjs(vendors.cancelDate) : undefined;
      });
    }
    return res;
  }
}
