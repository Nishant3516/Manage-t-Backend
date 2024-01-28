import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchoolPictureGallery, getSchoolPictureGalleryIdentifier } from '../school-picture-gallery.model';

export type EntityResponseType = HttpResponse<ISchoolPictureGallery>;
export type EntityArrayResponseType = HttpResponse<ISchoolPictureGallery[]>;

@Injectable({ providedIn: 'root' })
export class SchoolPictureGalleryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/school-picture-galleries');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(schoolPictureGallery: ISchoolPictureGallery): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolPictureGallery);
    return this.http
      .post<ISchoolPictureGallery>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(schoolPictureGallery: ISchoolPictureGallery): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolPictureGallery);
    return this.http
      .put<ISchoolPictureGallery>(`${this.resourceUrl}/${getSchoolPictureGalleryIdentifier(schoolPictureGallery) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(schoolPictureGallery: ISchoolPictureGallery): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolPictureGallery);
    return this.http
      .patch<ISchoolPictureGallery>(`${this.resourceUrl}/${getSchoolPictureGalleryIdentifier(schoolPictureGallery) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISchoolPictureGallery>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISchoolPictureGallery[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSchoolPictureGalleryToCollectionIfMissing(
    schoolPictureGalleryCollection: ISchoolPictureGallery[],
    ...schoolPictureGalleriesToCheck: (ISchoolPictureGallery | null | undefined)[]
  ): ISchoolPictureGallery[] {
    const schoolPictureGalleries: ISchoolPictureGallery[] = schoolPictureGalleriesToCheck.filter(isPresent);
    if (schoolPictureGalleries.length > 0) {
      const schoolPictureGalleryCollectionIdentifiers = schoolPictureGalleryCollection.map(
        schoolPictureGalleryItem => getSchoolPictureGalleryIdentifier(schoolPictureGalleryItem)!
      );
      const schoolPictureGalleriesToAdd = schoolPictureGalleries.filter(schoolPictureGalleryItem => {
        const schoolPictureGalleryIdentifier = getSchoolPictureGalleryIdentifier(schoolPictureGalleryItem);
        if (schoolPictureGalleryIdentifier == null || schoolPictureGalleryCollectionIdentifiers.includes(schoolPictureGalleryIdentifier)) {
          return false;
        }
        schoolPictureGalleryCollectionIdentifiers.push(schoolPictureGalleryIdentifier);
        return true;
      });
      return [...schoolPictureGalleriesToAdd, ...schoolPictureGalleryCollection];
    }
    return schoolPictureGalleryCollection;
  }

  protected convertDateFromClient(schoolPictureGallery: ISchoolPictureGallery): ISchoolPictureGallery {
    return Object.assign({}, schoolPictureGallery, {
      createDate: schoolPictureGallery.createDate?.isValid() ? schoolPictureGallery.createDate.format(DATE_FORMAT) : undefined,
      lastModified: schoolPictureGallery.lastModified?.isValid() ? schoolPictureGallery.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: schoolPictureGallery.cancelDate?.isValid() ? schoolPictureGallery.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((schoolPictureGallery: ISchoolPictureGallery) => {
        schoolPictureGallery.createDate = schoolPictureGallery.createDate ? dayjs(schoolPictureGallery.createDate) : undefined;
        schoolPictureGallery.lastModified = schoolPictureGallery.lastModified ? dayjs(schoolPictureGallery.lastModified) : undefined;
        schoolPictureGallery.cancelDate = schoolPictureGallery.cancelDate ? dayjs(schoolPictureGallery.cancelDate) : undefined;
      });
    }
    return res;
  }
}
