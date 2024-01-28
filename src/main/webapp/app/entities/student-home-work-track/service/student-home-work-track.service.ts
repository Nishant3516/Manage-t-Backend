import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudentHomeWorkTrack, getStudentHomeWorkTrackIdentifier } from '../student-home-work-track.model';

export type EntityResponseType = HttpResponse<IStudentHomeWorkTrack>;
export type EntityArrayResponseType = HttpResponse<IStudentHomeWorkTrack[]>;

@Injectable({ providedIn: 'root' })
export class StudentHomeWorkTrackService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/student-home-work-tracks');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(studentHomeWorkTrack: IStudentHomeWorkTrack): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentHomeWorkTrack);
    return this.http
      .post<IStudentHomeWorkTrack>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(studentHomeWorkTrack: IStudentHomeWorkTrack): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentHomeWorkTrack);
    return this.http
      .put<IStudentHomeWorkTrack>(`${this.resourceUrl}/${getStudentHomeWorkTrackIdentifier(studentHomeWorkTrack) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }
updateAll(studentAttendences: IStudentHomeWorkTrack []): Observable<EntityArrayResponseType> {
    // const copy = this.convertDateFromClient(studentAttendence);
    return this.http
      .put<IStudentHomeWorkTrack[]>(`${this.resourceUrl}`, studentAttendences, {
        observe: 'response',
      })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  partialUpdate(studentHomeWorkTrack: IStudentHomeWorkTrack): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentHomeWorkTrack);
    return this.http
      .patch<IStudentHomeWorkTrack>(`${this.resourceUrl}/${getStudentHomeWorkTrackIdentifier(studentHomeWorkTrack) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStudentHomeWorkTrack>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStudentHomeWorkTrack[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStudentHomeWorkTrackToCollectionIfMissing(
    studentHomeWorkTrackCollection: IStudentHomeWorkTrack[],
    ...studentHomeWorkTracksToCheck: (IStudentHomeWorkTrack | null | undefined)[]
  ): IStudentHomeWorkTrack[] {
    const studentHomeWorkTracks: IStudentHomeWorkTrack[] = studentHomeWorkTracksToCheck.filter(isPresent);
    if (studentHomeWorkTracks.length > 0) {
      const studentHomeWorkTrackCollectionIdentifiers = studentHomeWorkTrackCollection.map(
        studentHomeWorkTrackItem => getStudentHomeWorkTrackIdentifier(studentHomeWorkTrackItem)!
      );
      const studentHomeWorkTracksToAdd = studentHomeWorkTracks.filter(studentHomeWorkTrackItem => {
        const studentHomeWorkTrackIdentifier = getStudentHomeWorkTrackIdentifier(studentHomeWorkTrackItem);
        if (studentHomeWorkTrackIdentifier == null || studentHomeWorkTrackCollectionIdentifiers.includes(studentHomeWorkTrackIdentifier)) {
          return false;
        }
        studentHomeWorkTrackCollectionIdentifiers.push(studentHomeWorkTrackIdentifier);
        return true;
      });
      return [...studentHomeWorkTracksToAdd, ...studentHomeWorkTrackCollection];
    }
    return studentHomeWorkTrackCollection;
  }

  protected convertDateFromClient(studentHomeWorkTrack: IStudentHomeWorkTrack): IStudentHomeWorkTrack {
    return Object.assign({}, studentHomeWorkTrack, {
      createDate: studentHomeWorkTrack.createDate?.isValid() ? studentHomeWorkTrack.createDate.format(DATE_FORMAT) : undefined,
      lastModified: studentHomeWorkTrack.lastModified?.isValid() ? studentHomeWorkTrack.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: studentHomeWorkTrack.cancelDate?.isValid() ? studentHomeWorkTrack.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((studentHomeWorkTrack: IStudentHomeWorkTrack) => {
        studentHomeWorkTrack.createDate = studentHomeWorkTrack.createDate ? dayjs(studentHomeWorkTrack.createDate) : undefined;
        studentHomeWorkTrack.lastModified = studentHomeWorkTrack.lastModified ? dayjs(studentHomeWorkTrack.lastModified) : undefined;
        studentHomeWorkTrack.cancelDate = studentHomeWorkTrack.cancelDate ? dayjs(studentHomeWorkTrack.cancelDate) : undefined;
      });
    }
    return res;
  }
}
