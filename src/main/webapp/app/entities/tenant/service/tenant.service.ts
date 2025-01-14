import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITenant, getTenantIdentifier } from '../tenant.model';

export type EntityResponseType = HttpResponse<ITenant>;
export type EntityArrayResponseType = HttpResponse<ITenant[]>;

@Injectable({ providedIn: 'root' })
export class TenantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tenants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tenant: ITenant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tenant);
    return this.http
      .post<ITenant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tenant: ITenant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tenant);
    return this.http
      .put<ITenant>(`${this.resourceUrl}/${getTenantIdentifier(tenant) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tenant: ITenant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tenant);
    return this.http
      .patch<ITenant>(`${this.resourceUrl}/${getTenantIdentifier(tenant) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITenant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITenant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTenantToCollectionIfMissing(tenantCollection: ITenant[], ...tenantsToCheck: (ITenant | null | undefined)[]): ITenant[] {
    const tenants: ITenant[] = tenantsToCheck.filter(isPresent);
    if (tenants.length > 0) {
      const tenantCollectionIdentifiers = tenantCollection.map(tenantItem => getTenantIdentifier(tenantItem)!);
      const tenantsToAdd = tenants.filter(tenantItem => {
        const tenantIdentifier = getTenantIdentifier(tenantItem);
        if (tenantIdentifier == null || tenantCollectionIdentifiers.includes(tenantIdentifier)) {
          return false;
        }
        tenantCollectionIdentifiers.push(tenantIdentifier);
        return true;
      });
      return [...tenantsToAdd, ...tenantCollection];
    }
    return tenantCollection;
  }

  protected convertDateFromClient(tenant: ITenant): ITenant {
    return Object.assign({}, tenant, {
      createDate: tenant.createDate?.isValid() ? tenant.createDate.format(DATE_FORMAT) : undefined,
      lastModified: tenant.lastModified?.isValid() ? tenant.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: tenant.cancelDate?.isValid() ? tenant.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((tenant: ITenant) => {
        tenant.createDate = tenant.createDate ? dayjs(tenant.createDate) : undefined;
        tenant.lastModified = tenant.lastModified ? dayjs(tenant.lastModified) : undefined;
        tenant.cancelDate = tenant.cancelDate ? dayjs(tenant.cancelDate) : undefined;
      });
    }
    return res;
  }
}
