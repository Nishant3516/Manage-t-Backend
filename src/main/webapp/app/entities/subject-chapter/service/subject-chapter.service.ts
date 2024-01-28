import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubjectChapter, getSubjectChapterIdentifier } from '../subject-chapter.model';

export type EntityResponseType = HttpResponse<ISubjectChapter>;
export type EntityArrayResponseType = HttpResponse<ISubjectChapter[]>;

@Injectable({ providedIn: 'root' })
export class SubjectChapterService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/subject-chapters');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(subjectChapter: ISubjectChapter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subjectChapter);
    return this.http
      .post<ISubjectChapter>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(subjectChapter: ISubjectChapter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subjectChapter);
    return this.http
      .put<ISubjectChapter>(`${this.resourceUrl}/${getSubjectChapterIdentifier(subjectChapter) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(subjectChapter: ISubjectChapter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subjectChapter);
    return this.http
      .patch<ISubjectChapter>(`${this.resourceUrl}/${getSubjectChapterIdentifier(subjectChapter) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISubjectChapter>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISubjectChapter[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSubjectChapterToCollectionIfMissing(
    subjectChapterCollection: ISubjectChapter[],
    ...subjectChaptersToCheck: (ISubjectChapter | null | undefined)[]
  ): ISubjectChapter[] {
    const subjectChapters: ISubjectChapter[] = subjectChaptersToCheck.filter(isPresent);
    if (subjectChapters.length > 0) {
      const subjectChapterCollectionIdentifiers = subjectChapterCollection.map(
        subjectChapterItem => getSubjectChapterIdentifier(subjectChapterItem)!
      );
      const subjectChaptersToAdd = subjectChapters.filter(subjectChapterItem => {
        const subjectChapterIdentifier = getSubjectChapterIdentifier(subjectChapterItem);
        if (subjectChapterIdentifier == null || subjectChapterCollectionIdentifiers.includes(subjectChapterIdentifier)) {
          return false;
        }
        subjectChapterCollectionIdentifiers.push(subjectChapterIdentifier);
        return true;
      });
      return [...subjectChaptersToAdd, ...subjectChapterCollection];
    }
    return subjectChapterCollection;
  }

  protected convertDateFromClient(subjectChapter: ISubjectChapter): ISubjectChapter {
    return Object.assign({}, subjectChapter, {
      createDate: subjectChapter.createDate?.isValid() ? subjectChapter.createDate.format(DATE_FORMAT) : undefined,
      lastModified: subjectChapter.lastModified?.isValid() ? subjectChapter.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: subjectChapter.cancelDate?.isValid() ? subjectChapter.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((subjectChapter: ISubjectChapter) => {
        subjectChapter.createDate = subjectChapter.createDate ? dayjs(subjectChapter.createDate) : undefined;
        subjectChapter.lastModified = subjectChapter.lastModified ? dayjs(subjectChapter.lastModified) : undefined;
        subjectChapter.cancelDate = subjectChapter.cancelDate ? dayjs(subjectChapter.cancelDate) : undefined;
      });
    }
    return res;
  }
}
