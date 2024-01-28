import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchoolClass, getSchoolClassIdentifier } from '../school-class.model';

export type EntityResponseType = HttpResponse<ISchoolClass>;
export type EntityArrayResponseType = HttpResponse<ISchoolClass[]>;

@Injectable({ providedIn: 'root' })
export class SchoolClassService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/school-classes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(schoolClass: ISchoolClass): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolClass);
    return this.http
      .post<ISchoolClass>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(schoolClass: ISchoolClass): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolClass);
    return this.http
      .put<ISchoolClass>(`${this.resourceUrl}/${getSchoolClassIdentifier(schoolClass) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(schoolClass: ISchoolClass): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolClass);
    return this.http
      .patch<ISchoolClass>(`${this.resourceUrl}/${getSchoolClassIdentifier(schoolClass) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISchoolClass>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISchoolClass[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSchoolClassToCollectionIfMissing(
    schoolClassCollection: ISchoolClass[],
    ...schoolClassesToCheck: (ISchoolClass | null | undefined)[]
  ): ISchoolClass[] {
    const schoolClasses: ISchoolClass[] = schoolClassesToCheck.filter(isPresent);
    if (schoolClasses.length > 0) {
      const schoolClassCollectionIdentifiers = schoolClassCollection.map(schoolClassItem => getSchoolClassIdentifier(schoolClassItem)!);
      const schoolClassesToAdd = schoolClasses.filter(schoolClassItem => {
        const schoolClassIdentifier = getSchoolClassIdentifier(schoolClassItem);
        if (schoolClassIdentifier == null || schoolClassCollectionIdentifiers.includes(schoolClassIdentifier)) {
          return false;
        }
        schoolClassCollectionIdentifiers.push(schoolClassIdentifier);
        return true;
      });
      return [...schoolClassesToAdd, ...schoolClassCollection];
    }
    return schoolClassCollection;
  }

  protected convertDateFromClient(schoolClass: ISchoolClass): ISchoolClass {
    return Object.assign({}, schoolClass, {
      createDate: schoolClass.createDate?.isValid() ? schoolClass.createDate.format(DATE_FORMAT) : undefined,
      lastModified: schoolClass.lastModified?.isValid() ? schoolClass.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: schoolClass.cancelDate?.isValid() ? schoolClass.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((schoolClass: ISchoolClass) => {
        schoolClass.createDate = schoolClass.createDate ? dayjs(schoolClass.createDate) : undefined;
        schoolClass.lastModified = schoolClass.lastModified ? dayjs(schoolClass.lastModified) : undefined;
        schoolClass.cancelDate = schoolClass.cancelDate ? dayjs(schoolClass.cancelDate) : undefined;
      });
    }
    return res;
  }
}
