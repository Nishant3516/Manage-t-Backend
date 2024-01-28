import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassClassWork, getClassClassWorkIdentifier } from '../class-class-work.model';

export type EntityResponseType = HttpResponse<IClassClassWork>;
export type EntityArrayResponseType = HttpResponse<IClassClassWork[]>;

@Injectable({ providedIn: 'root' })
export class ClassClassWorkService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/class-class-works');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(classClassWork: IClassClassWork): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classClassWork);
    return this.http
      .post<IClassClassWork>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(classClassWork: IClassClassWork): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classClassWork);
    return this.http
      .put<IClassClassWork>(`${this.resourceUrl}/${getClassClassWorkIdentifier(classClassWork) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(classClassWork: IClassClassWork): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classClassWork);
    return this.http
      .patch<IClassClassWork>(`${this.resourceUrl}/${getClassClassWorkIdentifier(classClassWork) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IClassClassWork>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClassClassWork[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClassClassWorkToCollectionIfMissing(
    classClassWorkCollection: IClassClassWork[],
    ...classClassWorksToCheck: (IClassClassWork | null | undefined)[]
  ): IClassClassWork[] {
    const classClassWorks: IClassClassWork[] = classClassWorksToCheck.filter(isPresent);
    if (classClassWorks.length > 0) {
      const classClassWorkCollectionIdentifiers = classClassWorkCollection.map(
        classClassWorkItem => getClassClassWorkIdentifier(classClassWorkItem)!
      );
      const classClassWorksToAdd = classClassWorks.filter(classClassWorkItem => {
        const classClassWorkIdentifier = getClassClassWorkIdentifier(classClassWorkItem);
        if (classClassWorkIdentifier == null || classClassWorkCollectionIdentifiers.includes(classClassWorkIdentifier)) {
          return false;
        }
        classClassWorkCollectionIdentifiers.push(classClassWorkIdentifier);
        return true;
      });
      return [...classClassWorksToAdd, ...classClassWorkCollection];
    }
    return classClassWorkCollection;
  }

  protected convertDateFromClient(classClassWork: IClassClassWork): IClassClassWork {
    return Object.assign({}, classClassWork, {
      schoolDate: classClassWork.schoolDate?.isValid() ? classClassWork.schoolDate.format(DATE_FORMAT) : undefined,
      createDate: classClassWork.createDate?.isValid() ? classClassWork.createDate.format(DATE_FORMAT) : undefined,
      lastModified: classClassWork.lastModified?.isValid() ? classClassWork.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: classClassWork.cancelDate?.isValid() ? classClassWork.cancelDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.schoolDate = res.body.schoolDate ? dayjs(res.body.schoolDate) : undefined;
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
      res.body.cancelDate = res.body.cancelDate ? dayjs(res.body.cancelDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((classClassWork: IClassClassWork) => {
        classClassWork.schoolDate = classClassWork.schoolDate ? dayjs(classClassWork.schoolDate) : undefined;
        classClassWork.createDate = classClassWork.createDate ? dayjs(classClassWork.createDate) : undefined;
        classClassWork.lastModified = classClassWork.lastModified ? dayjs(classClassWork.lastModified) : undefined;
        classClassWork.cancelDate = classClassWork.cancelDate ? dayjs(classClassWork.cancelDate) : undefined;
      });
    }
    return res;
  }
}
