import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudentAttendence, getStudentAttendenceIdentifier } from '../student-attendence.model';

export type EntityResponseType = HttpResponse<IStudentAttendence>;
export type EntityArrayResponseType = HttpResponse<IStudentAttendence[]>;

@Injectable({ providedIn: 'root' })
export class StudentAttendenceService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/student-attendences');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(studentAttendence: IStudentAttendence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentAttendence);
    return this.http
      .post<IStudentAttendence>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(studentAttendence: IStudentAttendence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentAttendence);
    return this.http
      .put<IStudentAttendence>(`${this.resourceUrl}/${getStudentAttendenceIdentifier(studentAttendence) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateAll(studentAttendences: IStudentAttendence []): Observable<EntityArrayResponseType> {
    // const copy = this.convertDateFromClient(studentAttendence);
    return this.http
      .put<IStudentAttendence[]>(`${this.resourceUrl}`, studentAttendences, {
        observe: 'response',
      })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }


  partialUpdate(studentAttendence: IStudentAttendence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentAttendence);
    return this.http
      .patch<IStudentAttendence>(`${this.resourceUrl}/${getStudentAttendenceIdentifier(studentAttendence) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStudentAttendence>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStudentAttendence[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStudentAttendenceToCollectionIfMissing(
    studentAttendenceCollection: IStudentAttendence[],
    ...studentAttendencesToCheck: (IStudentAttendence | null | undefined)[]
  ): IStudentAttendence[] {
    const studentAttendences: IStudentAttendence[] = studentAttendencesToCheck.filter(isPresent);
    if (studentAttendences.length > 0) {
      const studentAttendenceCollectionIdentifiers = studentAttendenceCollection.map(
        studentAttendenceItem => getStudentAttendenceIdentifier(studentAttendenceItem)!
      );
      const studentAttendencesToAdd = studentAttendences.filter(studentAttendenceItem => {
        const studentAttendenceIdentifier = getStudentAttendenceIdentifier(studentAttendenceItem);
        if (studentAttendenceIdentifier == null || studentAttendenceCollectionIdentifiers.includes(studentAttendenceIdentifier)) {
          return false;
        }
        studentAttendenceCollectionIdentifiers.push(studentAttendenceIdentifier);
        return true;
      });
      return [...studentAttendencesToAdd, ...studentAttendenceCollection];
    }
    return studentAttendenceCollection;
  }

  protected convertDateFromClient(studentAttendence: IStudentAttendence): IStudentAttendence {
    return Object.assign({}, studentAttendence, {
      schoolDate: studentAttendence.schoolDate?.isValid() ? studentAttendence.schoolDate.format(DATE_FORMAT) : undefined,
      createDate: studentAttendence.createDate?.isValid() ? studentAttendence.createDate.format(DATE_FORMAT) : undefined,
      lastModified: studentAttendence.lastModified?.isValid() ? studentAttendence.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: studentAttendence.cancelDate?.isValid() ? studentAttendence.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((studentAttendence: IStudentAttendence) => {
        studentAttendence.schoolDate = studentAttendence.schoolDate ? dayjs(studentAttendence.schoolDate) : undefined;
        studentAttendence.createDate = studentAttendence.createDate ? dayjs(studentAttendence.createDate) : undefined;
        studentAttendence.lastModified = studentAttendence.lastModified ? dayjs(studentAttendence.lastModified) : undefined;
        studentAttendence.cancelDate = studentAttendence.cancelDate ? dayjs(studentAttendence.cancelDate) : undefined;
      });
    }
    return res;
  }
}
