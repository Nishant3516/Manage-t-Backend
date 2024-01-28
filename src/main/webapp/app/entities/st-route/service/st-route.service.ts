import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISTRoute, getSTRouteIdentifier } from '../st-route.model';

export type EntityResponseType = HttpResponse<ISTRoute>;
export type EntityArrayResponseType = HttpResponse<ISTRoute[]>;

@Injectable({ providedIn: 'root' })
export class STRouteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/st-routes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sTRoute: ISTRoute): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sTRoute);
    return this.http
      .post<ISTRoute>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sTRoute: ISTRoute): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sTRoute);
    return this.http
      .put<ISTRoute>(`${this.resourceUrl}/${getSTRouteIdentifier(sTRoute) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(sTRoute: ISTRoute): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sTRoute);
    return this.http
      .patch<ISTRoute>(`${this.resourceUrl}/${getSTRouteIdentifier(sTRoute) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISTRoute>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISTRoute[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSTRouteToCollectionIfMissing(sTRouteCollection: ISTRoute[], ...sTRoutesToCheck: (ISTRoute | null | undefined)[]): ISTRoute[] {
    const sTRoutes: ISTRoute[] = sTRoutesToCheck.filter(isPresent);
    if (sTRoutes.length > 0) {
      const sTRouteCollectionIdentifiers = sTRouteCollection.map(sTRouteItem => getSTRouteIdentifier(sTRouteItem)!);
      const sTRoutesToAdd = sTRoutes.filter(sTRouteItem => {
        const sTRouteIdentifier = getSTRouteIdentifier(sTRouteItem);
        if (sTRouteIdentifier == null || sTRouteCollectionIdentifiers.includes(sTRouteIdentifier)) {
          return false;
        }
        sTRouteCollectionIdentifiers.push(sTRouteIdentifier);
        return true;
      });
      return [...sTRoutesToAdd, ...sTRouteCollection];
    }
    return sTRouteCollection;
  }

  protected convertDateFromClient(sTRoute: ISTRoute): ISTRoute {
    return Object.assign({}, sTRoute, {
      createDate: sTRoute.createDate?.isValid() ? sTRoute.createDate.format(DATE_FORMAT) : undefined,
      cancelDate: sTRoute.cancelDate?.isValid() ? sTRoute.cancelDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.cancelDate = res.body.cancelDate ? dayjs(res.body.cancelDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((sTRoute: ISTRoute) => {
        sTRoute.createDate = sTRoute.createDate ? dayjs(sTRoute.createDate) : undefined;
        sTRoute.cancelDate = sTRoute.cancelDate ? dayjs(sTRoute.cancelDate) : undefined;
      });
    }
    return res;
  }
}
