import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChapterSection, getChapterSectionIdentifier } from '../chapter-section.model';

export type EntityResponseType = HttpResponse<IChapterSection>;
export type EntityArrayResponseType = HttpResponse<IChapterSection[]>;

@Injectable({ providedIn: 'root' })
export class ChapterSectionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/chapter-sections');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(chapterSection: IChapterSection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chapterSection);
    return this.http
      .post<IChapterSection>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(chapterSection: IChapterSection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chapterSection);
    return this.http
      .put<IChapterSection>(`${this.resourceUrl}/${getChapterSectionIdentifier(chapterSection) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(chapterSection: IChapterSection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chapterSection);
    return this.http
      .patch<IChapterSection>(`${this.resourceUrl}/${getChapterSectionIdentifier(chapterSection) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChapterSection>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChapterSection[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChapterSectionToCollectionIfMissing(
    chapterSectionCollection: IChapterSection[],
    ...chapterSectionsToCheck: (IChapterSection | null | undefined)[]
  ): IChapterSection[] {
    const chapterSections: IChapterSection[] = chapterSectionsToCheck.filter(isPresent);
    if (chapterSections.length > 0) {
      const chapterSectionCollectionIdentifiers = chapterSectionCollection.map(
        chapterSectionItem => getChapterSectionIdentifier(chapterSectionItem)!
      );
      const chapterSectionsToAdd = chapterSections.filter(chapterSectionItem => {
        const chapterSectionIdentifier = getChapterSectionIdentifier(chapterSectionItem);
        if (chapterSectionIdentifier == null || chapterSectionCollectionIdentifiers.includes(chapterSectionIdentifier)) {
          return false;
        }
        chapterSectionCollectionIdentifiers.push(chapterSectionIdentifier);
        return true;
      });
      return [...chapterSectionsToAdd, ...chapterSectionCollection];
    }
    return chapterSectionCollection;
  }

  protected convertDateFromClient(chapterSection: IChapterSection): IChapterSection {
    return Object.assign({}, chapterSection, {
      createDate: chapterSection.createDate?.isValid() ? chapterSection.createDate.format(DATE_FORMAT) : undefined,
      lastModified: chapterSection.lastModified?.isValid() ? chapterSection.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: chapterSection.cancelDate?.isValid() ? chapterSection.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((chapterSection: IChapterSection) => {
        chapterSection.createDate = chapterSection.createDate ? dayjs(chapterSection.createDate) : undefined;
        chapterSection.lastModified = chapterSection.lastModified ? dayjs(chapterSection.lastModified) : undefined;
        chapterSection.cancelDate = chapterSection.cancelDate ? dayjs(chapterSection.cancelDate) : undefined;
      });
    }
    return res;
  }
}
