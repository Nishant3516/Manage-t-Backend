import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchoolNotifications, getSchoolNotificationsIdentifier } from '../school-notifications.model';

export type EntityResponseType = HttpResponse<ISchoolNotifications>;
export type EntityArrayResponseType = HttpResponse<ISchoolNotifications[]>;

@Injectable({ providedIn: 'root' })
export class SchoolNotificationsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/school-notifications');
  public publicResourceUrl = this.applicationConfigService.getEndpointFor('public/school-notifications');
  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(schoolNotifications: ISchoolNotifications): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolNotifications);
    return this.http
      .post<ISchoolNotifications>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(schoolNotifications: ISchoolNotifications): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolNotifications);
    return this.http
      .put<ISchoolNotifications>(`${this.resourceUrl}/${getSchoolNotificationsIdentifier(schoolNotifications) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(schoolNotifications: ISchoolNotifications): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolNotifications);
    return this.http
      .patch<ISchoolNotifications>(`${this.resourceUrl}/${getSchoolNotificationsIdentifier(schoolNotifications) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISchoolNotifications>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISchoolNotifications[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  queryPublic(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISchoolNotifications[]>(this.publicResourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSchoolNotificationsToCollectionIfMissing(
    schoolNotificationsCollection: ISchoolNotifications[],
    ...schoolNotificationsToCheck: (ISchoolNotifications | null | undefined)[]
  ): ISchoolNotifications[] {
    const schoolNotifications: ISchoolNotifications[] = schoolNotificationsToCheck.filter(isPresent);
    if (schoolNotifications.length > 0) {
      const schoolNotificationsCollectionIdentifiers = schoolNotificationsCollection.map(
        schoolNotificationsItem => getSchoolNotificationsIdentifier(schoolNotificationsItem)!
      );
      const schoolNotificationsToAdd = schoolNotifications.filter(schoolNotificationsItem => {
        const schoolNotificationsIdentifier = getSchoolNotificationsIdentifier(schoolNotificationsItem);
        if (schoolNotificationsIdentifier == null || schoolNotificationsCollectionIdentifiers.includes(schoolNotificationsIdentifier)) {
          return false;
        }
        schoolNotificationsCollectionIdentifiers.push(schoolNotificationsIdentifier);
        return true;
      });
      return [...schoolNotificationsToAdd, ...schoolNotificationsCollection];
    }
    return schoolNotificationsCollection;
  }

  protected convertDateFromClient(schoolNotifications: ISchoolNotifications): ISchoolNotifications {
    return Object.assign({}, schoolNotifications, {
      showTillDate: schoolNotifications.showTillDate?.isValid() ? schoolNotifications.showTillDate.format(DATE_FORMAT) : undefined,
      createDate: schoolNotifications.createDate?.isValid() ? schoolNotifications.createDate.format(DATE_FORMAT) : undefined,
      lastModified: schoolNotifications.lastModified?.isValid() ? schoolNotifications.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: schoolNotifications.cancelDate?.isValid() ? schoolNotifications.cancelDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.showTillDate = res.body.showTillDate ? dayjs(res.body.showTillDate) : undefined;
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
      res.body.cancelDate = res.body.cancelDate ? dayjs(res.body.cancelDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((schoolNotifications: ISchoolNotifications) => {
        schoolNotifications.showTillDate = schoolNotifications.showTillDate ? dayjs(schoolNotifications.showTillDate) : undefined;
        schoolNotifications.createDate = schoolNotifications.createDate ? dayjs(schoolNotifications.createDate) : undefined;
        schoolNotifications.lastModified = schoolNotifications.lastModified ? dayjs(schoolNotifications.lastModified) : undefined;
        schoolNotifications.cancelDate = schoolNotifications.cancelDate ? dayjs(schoolNotifications.cancelDate) : undefined;
      });
    }
    return res;
  }
}
