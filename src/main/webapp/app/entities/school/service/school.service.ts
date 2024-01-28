import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchool, getSchoolIdentifier } from '../school.model';

export type EntityResponseType = HttpResponse<ISchool>;
export type EntityArrayResponseType = HttpResponse<ISchool[]>;

@Injectable({ providedIn: 'root' })
export class SchoolService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/schools');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(school: ISchool): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(school);
    return this.http
      .post<ISchool>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(school: ISchool): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(school);
    return this.http
      .put<ISchool>(`${this.resourceUrl}/${getSchoolIdentifier(school) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(school: ISchool): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(school);
    return this.http
      .patch<ISchool>(`${this.resourceUrl}/${getSchoolIdentifier(school) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISchool>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISchool[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSchoolToCollectionIfMissing(schoolCollection: ISchool[], ...schoolsToCheck: (ISchool | null | undefined)[]): ISchool[] {
    const schools: ISchool[] = schoolsToCheck.filter(isPresent);
    if (schools.length > 0) {
      const schoolCollectionIdentifiers = schoolCollection.map(schoolItem => getSchoolIdentifier(schoolItem)!);
      const schoolsToAdd = schools.filter(schoolItem => {
        const schoolIdentifier = getSchoolIdentifier(schoolItem);
        if (schoolIdentifier == null || schoolCollectionIdentifiers.includes(schoolIdentifier)) {
          return false;
        }
        schoolCollectionIdentifiers.push(schoolIdentifier);
        return true;
      });
      return [...schoolsToAdd, ...schoolCollection];
    }
    return schoolCollection;
  }

  protected convertDateFromClient(school: ISchool): ISchool {
    return Object.assign({}, school, {
      createDate: school.createDate?.isValid() ? school.createDate.format(DATE_FORMAT) : undefined,
      lastModified: school.lastModified?.isValid() ? school.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: school.cancelDate?.isValid() ? school.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((school: ISchool) => {
        school.createDate = school.createDate ? dayjs(school.createDate) : undefined;
        school.lastModified = school.lastModified ? dayjs(school.lastModified) : undefined;
        school.cancelDate = school.cancelDate ? dayjs(school.cancelDate) : undefined;
      });
    }
    return res;
  }
}
