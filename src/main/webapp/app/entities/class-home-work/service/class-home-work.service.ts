import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassHomeWork, getClassHomeWorkIdentifier } from '../class-home-work.model';

export type EntityResponseType = HttpResponse<IClassHomeWork>;
export type EntityArrayResponseType = HttpResponse<IClassHomeWork[]>;

@Injectable({ providedIn: 'root' })
export class ClassHomeWorkService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/class-home-works');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(classHomeWork: IClassHomeWork): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classHomeWork);
    return this.http
      .post<IClassHomeWork>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(classHomeWork: IClassHomeWork): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classHomeWork);
    return this.http
      .put<IClassHomeWork>(`${this.resourceUrl}/${getClassHomeWorkIdentifier(classHomeWork) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(classHomeWork: IClassHomeWork): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classHomeWork);
    return this.http
      .patch<IClassHomeWork>(`${this.resourceUrl}/${getClassHomeWorkIdentifier(classHomeWork) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IClassHomeWork>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClassHomeWork[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClassHomeWorkToCollectionIfMissing(
    classHomeWorkCollection: IClassHomeWork[],
    ...classHomeWorksToCheck: (IClassHomeWork | null | undefined)[]
  ): IClassHomeWork[] {
    const classHomeWorks: IClassHomeWork[] = classHomeWorksToCheck.filter(isPresent);
    if (classHomeWorks.length > 0) {
      const classHomeWorkCollectionIdentifiers = classHomeWorkCollection.map(
        classHomeWorkItem => getClassHomeWorkIdentifier(classHomeWorkItem)!
      );
      const classHomeWorksToAdd = classHomeWorks.filter(classHomeWorkItem => {
        const classHomeWorkIdentifier = getClassHomeWorkIdentifier(classHomeWorkItem);
        if (classHomeWorkIdentifier == null || classHomeWorkCollectionIdentifiers.includes(classHomeWorkIdentifier)) {
          return false;
        }
        classHomeWorkCollectionIdentifiers.push(classHomeWorkIdentifier);
        return true;
      });
      return [...classHomeWorksToAdd, ...classHomeWorkCollection];
    }
    return classHomeWorkCollection;
  }

  protected convertDateFromClient(classHomeWork: IClassHomeWork): IClassHomeWork {
    return Object.assign({}, classHomeWork, {
      schoolDate: classHomeWork.schoolDate?.isValid() ? classHomeWork.schoolDate.format(DATE_FORMAT) : undefined,
      createDate: classHomeWork.createDate?.isValid() ? classHomeWork.createDate.format(DATE_FORMAT) : undefined,
      lastModified: classHomeWork.lastModified?.isValid() ? classHomeWork.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: classHomeWork.cancelDate?.isValid() ? classHomeWork.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((classHomeWork: IClassHomeWork) => {
        classHomeWork.schoolDate = classHomeWork.schoolDate ? dayjs(classHomeWork.schoolDate) : undefined;
        classHomeWork.createDate = classHomeWork.createDate ? dayjs(classHomeWork.createDate) : undefined;
        classHomeWork.lastModified = classHomeWork.lastModified ? dayjs(classHomeWork.lastModified) : undefined;
        classHomeWork.cancelDate = classHomeWork.cancelDate ? dayjs(classHomeWork.cancelDate) : undefined;
      });
    }
    return res;
  }
}
