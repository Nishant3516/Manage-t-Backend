import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassSubject, getClassSubjectIdentifier } from '../class-subject.model';

export type EntityResponseType = HttpResponse<IClassSubject>;
export type EntityArrayResponseType = HttpResponse<IClassSubject[]>;

@Injectable({ providedIn: 'root' })
export class ClassSubjectService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/class-subjects');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(classSubject: IClassSubject): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classSubject);
    return this.http
      .post<IClassSubject>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(classSubject: IClassSubject): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classSubject);
    return this.http
      .put<IClassSubject>(`${this.resourceUrl}/${getClassSubjectIdentifier(classSubject) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(classSubject: IClassSubject): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classSubject);
    return this.http
      .patch<IClassSubject>(`${this.resourceUrl}/${getClassSubjectIdentifier(classSubject) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IClassSubject>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClassSubject[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClassSubjectToCollectionIfMissing(
    classSubjectCollection: IClassSubject[],
    ...classSubjectsToCheck: (IClassSubject | null | undefined)[]
  ): IClassSubject[] {
    const classSubjects: IClassSubject[] = classSubjectsToCheck.filter(isPresent);
    if (classSubjects.length > 0) {
      const classSubjectCollectionIdentifiers = classSubjectCollection.map(
        classSubjectItem => getClassSubjectIdentifier(classSubjectItem)!
      );
      const classSubjectsToAdd = classSubjects.filter(classSubjectItem => {
        const classSubjectIdentifier = getClassSubjectIdentifier(classSubjectItem);
        if (classSubjectIdentifier == null || classSubjectCollectionIdentifiers.includes(classSubjectIdentifier)) {
          return false;
        }
        classSubjectCollectionIdentifiers.push(classSubjectIdentifier);
        return true;
      });
      return [...classSubjectsToAdd, ...classSubjectCollection];
    }
    return classSubjectCollection;
  }

  protected convertDateFromClient(classSubject: IClassSubject): IClassSubject {
    return Object.assign({}, classSubject, {
      createDate: classSubject.createDate?.isValid() ? classSubject.createDate.format(DATE_FORMAT) : undefined,
      lastModified: classSubject.lastModified?.isValid() ? classSubject.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: classSubject.cancelDate?.isValid() ? classSubject.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((classSubject: IClassSubject) => {
        classSubject.createDate = classSubject.createDate ? dayjs(classSubject.createDate) : undefined;
        classSubject.lastModified = classSubject.lastModified ? dayjs(classSubject.lastModified) : undefined;
        classSubject.cancelDate = classSubject.cancelDate ? dayjs(classSubject.cancelDate) : undefined;
      });
    }
    return res;
  }
}
