import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassLessionPlanTrack, getClassLessionPlanTrackIdentifier } from '../class-lession-plan-track.model';

export type EntityResponseType = HttpResponse<IClassLessionPlanTrack>;
export type EntityArrayResponseType = HttpResponse<IClassLessionPlanTrack[]>;

@Injectable({ providedIn: 'root' })
export class ClassLessionPlanTrackService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/class-lession-plan-tracks');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(classLessionPlanTrack: IClassLessionPlanTrack): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classLessionPlanTrack);
    return this.http
      .post<IClassLessionPlanTrack>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(classLessionPlanTrack: IClassLessionPlanTrack): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classLessionPlanTrack);
    return this.http
      .put<IClassLessionPlanTrack>(`${this.resourceUrl}/${getClassLessionPlanTrackIdentifier(classLessionPlanTrack) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }
 updateAll(studentAttendences: IClassLessionPlanTrack []): Observable<EntityArrayResponseType> {
    // const copy = this.convertDateFromClient(studentAttendence);
    return this.http
      .put<IClassLessionPlanTrack[]>(`${this.resourceUrl}`, studentAttendences, {
        observe: 'response',
      })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  partialUpdate(classLessionPlanTrack: IClassLessionPlanTrack): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classLessionPlanTrack);
    return this.http
      .patch<IClassLessionPlanTrack>(`${this.resourceUrl}/${getClassLessionPlanTrackIdentifier(classLessionPlanTrack) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IClassLessionPlanTrack>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClassLessionPlanTrack[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClassLessionPlanTrackToCollectionIfMissing(
    classLessionPlanTrackCollection: IClassLessionPlanTrack[],
    ...classLessionPlanTracksToCheck: (IClassLessionPlanTrack | null | undefined)[]
  ): IClassLessionPlanTrack[] {
    const classLessionPlanTracks: IClassLessionPlanTrack[] = classLessionPlanTracksToCheck.filter(isPresent);
    if (classLessionPlanTracks.length > 0) {
      const classLessionPlanTrackCollectionIdentifiers = classLessionPlanTrackCollection.map(
        classLessionPlanTrackItem => getClassLessionPlanTrackIdentifier(classLessionPlanTrackItem)!
      );
      const classLessionPlanTracksToAdd = classLessionPlanTracks.filter(classLessionPlanTrackItem => {
        const classLessionPlanTrackIdentifier = getClassLessionPlanTrackIdentifier(classLessionPlanTrackItem);
        if (
          classLessionPlanTrackIdentifier == null ||
          classLessionPlanTrackCollectionIdentifiers.includes(classLessionPlanTrackIdentifier)
        ) {
          return false;
        }
        classLessionPlanTrackCollectionIdentifiers.push(classLessionPlanTrackIdentifier);
        return true;
      });
      return [...classLessionPlanTracksToAdd, ...classLessionPlanTrackCollection];
    }
    return classLessionPlanTrackCollection;
  }

  protected convertDateFromClient(classLessionPlanTrack: IClassLessionPlanTrack): IClassLessionPlanTrack {
    return Object.assign({}, classLessionPlanTrack, {
      createDate: classLessionPlanTrack.createDate?.isValid() ? classLessionPlanTrack.createDate.format(DATE_FORMAT) : undefined,
      lastModified: classLessionPlanTrack.lastModified?.isValid() ? classLessionPlanTrack.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: classLessionPlanTrack.cancelDate?.isValid() ? classLessionPlanTrack.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((classLessionPlanTrack: IClassLessionPlanTrack) => {
        classLessionPlanTrack.createDate = classLessionPlanTrack.createDate ? dayjs(classLessionPlanTrack.createDate) : undefined;
        classLessionPlanTrack.lastModified = classLessionPlanTrack.lastModified ? dayjs(classLessionPlanTrack.lastModified) : undefined;
        classLessionPlanTrack.cancelDate = classLessionPlanTrack.cancelDate ? dayjs(classLessionPlanTrack.cancelDate) : undefined;
      });
    }
    return res;
  }
}
