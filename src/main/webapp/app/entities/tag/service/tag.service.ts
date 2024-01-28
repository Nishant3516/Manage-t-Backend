import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';
import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITag, getTagIdentifier } from '../tag.model';

export type EntityResponseType = HttpResponse<ITag>;
export type EntityArrayResponseType = HttpResponse<ITag[]>;

@Injectable({ providedIn: 'root' })
export class TagService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tags');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tag: ITag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tag);
    return this.http
      .post<ITag>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tag: ITag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tag);
    return this.http
      .put<ITag>(`${this.resourceUrl}/${getTagIdentifier(tag) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tag: ITag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tag);
    return this.http
      .patch<ITag>(`${this.resourceUrl}/${getTagIdentifier(tag) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITag>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITag[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTagToCollectionIfMissing(tagCollection: ITag[], ...tagsToCheck: (ITag | null | undefined)[]): ITag[] {
    const tags: ITag[] = tagsToCheck.filter(isPresent);
    if (tags.length > 0) {
      const tagCollectionIdentifiers = tagCollection.map(tagItem => getTagIdentifier(tagItem)!);
      const tagsToAdd = tags.filter(tagItem => {
        const tagIdentifier = getTagIdentifier(tagItem);
        if (tagIdentifier == null || tagCollectionIdentifiers.includes(tagIdentifier)) {
          return false;
        }
        tagCollectionIdentifiers.push(tagIdentifier);
        return true;
      });
      return [...tagsToAdd, ...tagCollection];
    }
    return tagCollection;
  }

  protected convertDateFromClient(tag: ITag): ITag {
    return Object.assign({}, tag, {
      createDate: tag.createDate?.isValid() ? tag.createDate.format(DATE_FORMAT) : undefined,
      lastModified: tag.lastModified?.isValid() ? tag.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: tag.cancelDate?.isValid() ? tag.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((tag: ITag) => {
        tag.createDate = tag.createDate ? dayjs(tag.createDate) : undefined;
        tag.lastModified = tag.lastModified ? dayjs(tag.lastModified) : undefined;
        tag.cancelDate = tag.cancelDate ? dayjs(tag.cancelDate) : undefined;
      });
    }
    return res;
  }
}
