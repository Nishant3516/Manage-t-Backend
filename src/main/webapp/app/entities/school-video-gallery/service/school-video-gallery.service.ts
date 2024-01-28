import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchoolVideoGallery, getSchoolVideoGalleryIdentifier } from '../school-video-gallery.model';

export type EntityResponseType = HttpResponse<ISchoolVideoGallery>;
export type EntityArrayResponseType = HttpResponse<ISchoolVideoGallery[]>;

@Injectable({ providedIn: 'root' })
export class SchoolVideoGalleryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/school-video-galleries');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(schoolVideoGallery: ISchoolVideoGallery): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolVideoGallery);
    return this.http
      .post<ISchoolVideoGallery>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(schoolVideoGallery: ISchoolVideoGallery): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolVideoGallery);
    return this.http
      .put<ISchoolVideoGallery>(`${this.resourceUrl}/${getSchoolVideoGalleryIdentifier(schoolVideoGallery) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(schoolVideoGallery: ISchoolVideoGallery): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolVideoGallery);
    return this.http
      .patch<ISchoolVideoGallery>(`${this.resourceUrl}/${getSchoolVideoGalleryIdentifier(schoolVideoGallery) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISchoolVideoGallery>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISchoolVideoGallery[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSchoolVideoGalleryToCollectionIfMissing(
    schoolVideoGalleryCollection: ISchoolVideoGallery[],
    ...schoolVideoGalleriesToCheck: (ISchoolVideoGallery | null | undefined)[]
  ): ISchoolVideoGallery[] {
    const schoolVideoGalleries: ISchoolVideoGallery[] = schoolVideoGalleriesToCheck.filter(isPresent);
    if (schoolVideoGalleries.length > 0) {
      const schoolVideoGalleryCollectionIdentifiers = schoolVideoGalleryCollection.map(
        schoolVideoGalleryItem => getSchoolVideoGalleryIdentifier(schoolVideoGalleryItem)!
      );
      const schoolVideoGalleriesToAdd = schoolVideoGalleries.filter(schoolVideoGalleryItem => {
        const schoolVideoGalleryIdentifier = getSchoolVideoGalleryIdentifier(schoolVideoGalleryItem);
        if (schoolVideoGalleryIdentifier == null || schoolVideoGalleryCollectionIdentifiers.includes(schoolVideoGalleryIdentifier)) {
          return false;
        }
        schoolVideoGalleryCollectionIdentifiers.push(schoolVideoGalleryIdentifier);
        return true;
      });
      return [...schoolVideoGalleriesToAdd, ...schoolVideoGalleryCollection];
    }
    return schoolVideoGalleryCollection;
  }

  protected convertDateFromClient(schoolVideoGallery: ISchoolVideoGallery): ISchoolVideoGallery {
    return Object.assign({}, schoolVideoGallery, {
      createDate: schoolVideoGallery.createDate?.isValid() ? schoolVideoGallery.createDate.format(DATE_FORMAT) : undefined,
      lastModified: schoolVideoGallery.lastModified?.isValid() ? schoolVideoGallery.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: schoolVideoGallery.cancelDate?.isValid() ? schoolVideoGallery.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((schoolVideoGallery: ISchoolVideoGallery) => {
        schoolVideoGallery.createDate = schoolVideoGallery.createDate ? dayjs(schoolVideoGallery.createDate) : undefined;
        schoolVideoGallery.lastModified = schoolVideoGallery.lastModified ? dayjs(schoolVideoGallery.lastModified) : undefined;
        schoolVideoGallery.cancelDate = schoolVideoGallery.cancelDate ? dayjs(schoolVideoGallery.cancelDate) : undefined;
      });
    }
    return res;
  }
}
