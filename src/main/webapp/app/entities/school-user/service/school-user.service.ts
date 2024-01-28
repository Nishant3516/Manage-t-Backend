import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchoolUser, getSchoolUserIdentifier } from '../school-user.model';

export type EntityResponseType = HttpResponse<ISchoolUser>;
export type EntityArrayResponseType = HttpResponse<ISchoolUser[]>;

@Injectable({ providedIn: 'root' })
export class SchoolUserService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/school-users');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(schoolUser: ISchoolUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolUser);
    return this.http
      .post<ISchoolUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(schoolUser: ISchoolUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolUser);
    return this.http
      .put<ISchoolUser>(`${this.resourceUrl}/${getSchoolUserIdentifier(schoolUser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(schoolUser: ISchoolUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolUser);
    return this.http
      .patch<ISchoolUser>(`${this.resourceUrl}/${getSchoolUserIdentifier(schoolUser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISchoolUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISchoolUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSchoolUserToCollectionIfMissing(
    schoolUserCollection: ISchoolUser[],
    ...schoolUsersToCheck: (ISchoolUser | null | undefined)[]
  ): ISchoolUser[] {
    const schoolUsers: ISchoolUser[] = schoolUsersToCheck.filter(isPresent);
    if (schoolUsers.length > 0) {
      const schoolUserCollectionIdentifiers = schoolUserCollection.map(schoolUserItem => getSchoolUserIdentifier(schoolUserItem)!);
      const schoolUsersToAdd = schoolUsers.filter(schoolUserItem => {
        const schoolUserIdentifier = getSchoolUserIdentifier(schoolUserItem);
        if (schoolUserIdentifier == null || schoolUserCollectionIdentifiers.includes(schoolUserIdentifier)) {
          return false;
        }
        schoolUserCollectionIdentifiers.push(schoolUserIdentifier);
        return true;
      });
      return [...schoolUsersToAdd, ...schoolUserCollection];
    }
    return schoolUserCollection;
  }

  protected convertDateFromClient(schoolUser: ISchoolUser): ISchoolUser {
    return Object.assign({}, schoolUser, {
      createDate: schoolUser.createDate?.isValid() ? schoolUser.createDate.format(DATE_FORMAT) : undefined,
      lastModified: schoolUser.lastModified?.isValid() ? schoolUser.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: schoolUser.cancelDate?.isValid() ? schoolUser.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((schoolUser: ISchoolUser) => {
        schoolUser.createDate = schoolUser.createDate ? dayjs(schoolUser.createDate) : undefined;
        schoolUser.lastModified = schoolUser.lastModified ? dayjs(schoolUser.lastModified) : undefined;
        schoolUser.cancelDate = schoolUser.cancelDate ? dayjs(schoolUser.cancelDate) : undefined;
      });
    }
    return res;
  }
}
