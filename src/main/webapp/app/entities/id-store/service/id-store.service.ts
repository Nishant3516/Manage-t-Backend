import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIdStore, getIdStoreIdentifier } from '../id-store.model';

export type EntityResponseType = HttpResponse<IIdStore>;
export type EntityArrayResponseType = HttpResponse<IIdStore[]>;

@Injectable({ providedIn: 'root' })
export class IdStoreService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/id-stores');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(idStore: IIdStore): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(idStore);
    return this.http
      .post<IIdStore>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(idStore: IIdStore): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(idStore);
    return this.http
      .put<IIdStore>(`${this.resourceUrl}/${getIdStoreIdentifier(idStore) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(idStore: IIdStore): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(idStore);
    return this.http
      .patch<IIdStore>(`${this.resourceUrl}/${getIdStoreIdentifier(idStore) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIdStore>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIdStore[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIdStoreToCollectionIfMissing(idStoreCollection: IIdStore[], ...idStoresToCheck: (IIdStore | null | undefined)[]): IIdStore[] {
    const idStores: IIdStore[] = idStoresToCheck.filter(isPresent);
    if (idStores.length > 0) {
      const idStoreCollectionIdentifiers = idStoreCollection.map(idStoreItem => getIdStoreIdentifier(idStoreItem)!);
      const idStoresToAdd = idStores.filter(idStoreItem => {
        const idStoreIdentifier = getIdStoreIdentifier(idStoreItem);
        if (idStoreIdentifier == null || idStoreCollectionIdentifiers.includes(idStoreIdentifier)) {
          return false;
        }
        idStoreCollectionIdentifiers.push(idStoreIdentifier);
        return true;
      });
      return [...idStoresToAdd, ...idStoreCollection];
    }
    return idStoreCollection;
  }

  protected convertDateFromClient(idStore: IIdStore): IIdStore {
    return Object.assign({}, idStore, {
      createDate: idStore.createDate?.isValid() ? idStore.createDate.format(DATE_FORMAT) : undefined,
      lastModified: idStore.lastModified?.isValid() ? idStore.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: idStore.cancelDate?.isValid() ? idStore.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((idStore: IIdStore) => {
        idStore.createDate = idStore.createDate ? dayjs(idStore.createDate) : undefined;
        idStore.lastModified = idStore.lastModified ? dayjs(idStore.lastModified) : undefined;
        idStore.cancelDate = idStore.cancelDate ? dayjs(idStore.cancelDate) : undefined;
      });
    }
    return res;
  }
}
