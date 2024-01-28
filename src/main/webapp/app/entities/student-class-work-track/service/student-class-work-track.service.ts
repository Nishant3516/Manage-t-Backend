import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudentClassWorkTrack, getStudentClassWorkTrackIdentifier } from '../student-class-work-track.model';

export type EntityResponseType = HttpResponse<IStudentClassWorkTrack>;
export type EntityArrayResponseType = HttpResponse<IStudentClassWorkTrack[]>;

@Injectable({ providedIn: 'root' })
export class StudentClassWorkTrackService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/student-class-work-tracks');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(studentClassWorkTrack: IStudentClassWorkTrack): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentClassWorkTrack);
    return this.http
      .post<IStudentClassWorkTrack>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(studentClassWorkTrack: IStudentClassWorkTrack): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentClassWorkTrack);
    return this.http
      .put<IStudentClassWorkTrack>(`${this.resourceUrl}/${getStudentClassWorkTrackIdentifier(studentClassWorkTrack) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }
updateAll(studentAttendences: IStudentClassWorkTrack []): Observable<EntityArrayResponseType> {
    // const copy = this.convertDateFromClient(studentAttendence);
    return this.http
      .put<IStudentClassWorkTrack[]>(`${this.resourceUrl}`, studentAttendences, {
        observe: 'response',
      })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  partialUpdate(studentClassWorkTrack: IStudentClassWorkTrack): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentClassWorkTrack);
    return this.http
      .patch<IStudentClassWorkTrack>(`${this.resourceUrl}/${getStudentClassWorkTrackIdentifier(studentClassWorkTrack) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStudentClassWorkTrack>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStudentClassWorkTrack[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStudentClassWorkTrackToCollectionIfMissing(
    studentClassWorkTrackCollection: IStudentClassWorkTrack[],
    ...studentClassWorkTracksToCheck: (IStudentClassWorkTrack | null | undefined)[]
  ): IStudentClassWorkTrack[] {
    const studentClassWorkTracks: IStudentClassWorkTrack[] = studentClassWorkTracksToCheck.filter(isPresent);
    if (studentClassWorkTracks.length > 0) {
      const studentClassWorkTrackCollectionIdentifiers = studentClassWorkTrackCollection.map(
        studentClassWorkTrackItem => getStudentClassWorkTrackIdentifier(studentClassWorkTrackItem)!
      );
      const studentClassWorkTracksToAdd = studentClassWorkTracks.filter(studentClassWorkTrackItem => {
        const studentClassWorkTrackIdentifier = getStudentClassWorkTrackIdentifier(studentClassWorkTrackItem);
        if (
          studentClassWorkTrackIdentifier == null ||
          studentClassWorkTrackCollectionIdentifiers.includes(studentClassWorkTrackIdentifier)
        ) {
          return false;
        }
        studentClassWorkTrackCollectionIdentifiers.push(studentClassWorkTrackIdentifier);
        return true;
      });
      return [...studentClassWorkTracksToAdd, ...studentClassWorkTrackCollection];
    }
    return studentClassWorkTrackCollection;
  }

  protected convertDateFromClient(studentClassWorkTrack: IStudentClassWorkTrack): IStudentClassWorkTrack {
    return Object.assign({}, studentClassWorkTrack, {
      createDate: studentClassWorkTrack.createDate?.isValid() ? studentClassWorkTrack.createDate.format(DATE_FORMAT) : undefined,
      lastModified: studentClassWorkTrack.lastModified?.isValid() ? studentClassWorkTrack.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: studentClassWorkTrack.cancelDate?.isValid() ? studentClassWorkTrack.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((studentClassWorkTrack: IStudentClassWorkTrack) => {
        studentClassWorkTrack.createDate = studentClassWorkTrack.createDate ? dayjs(studentClassWorkTrack.createDate) : undefined;
        studentClassWorkTrack.lastModified = studentClassWorkTrack.lastModified ? dayjs(studentClassWorkTrack.lastModified) : undefined;
        studentClassWorkTrack.cancelDate = studentClassWorkTrack.cancelDate ? dayjs(studentClassWorkTrack.cancelDate) : undefined;
      });
    }
    return res;
  }
}
