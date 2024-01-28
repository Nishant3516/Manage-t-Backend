import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassStudent, getClassStudentIdentifier } from '../class-student.model';

export type EntityResponseType = HttpResponse<IClassStudent>;
export type EntityArrayResponseType = HttpResponse<IClassStudent[]>;

@Injectable({ providedIn: 'root' })
export class ClassStudentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/class-students');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(classStudent: IClassStudent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classStudent);
    return this.http
      .post<IClassStudent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(classStudent: IClassStudent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classStudent);
    return this.http
      .put<IClassStudent>(`${this.resourceUrl}/${getClassStudentIdentifier(classStudent) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(classStudent: IClassStudent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classStudent);
    return this.http
      .patch<IClassStudent>(`${this.resourceUrl}/${getClassStudentIdentifier(classStudent) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IClassStudent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClassStudent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClassStudentToCollectionIfMissing(
    classStudentCollection: IClassStudent[],
    ...classStudentsToCheck: (IClassStudent | null | undefined)[]
  ): IClassStudent[] {
    const classStudents: IClassStudent[] = classStudentsToCheck.filter(isPresent);
    if (classStudents.length > 0) {
      const classStudentCollectionIdentifiers = classStudentCollection.map(
        classStudentItem => getClassStudentIdentifier(classStudentItem)!
      );
      const classStudentsToAdd = classStudents.filter(classStudentItem => {
        const classStudentIdentifier = getClassStudentIdentifier(classStudentItem);
        if (classStudentIdentifier == null || classStudentCollectionIdentifiers.includes(classStudentIdentifier)) {
          return false;
        }
        classStudentCollectionIdentifiers.push(classStudentIdentifier);
        return true;
      });
      return [...classStudentsToAdd, ...classStudentCollection];
    }
    return classStudentCollection;
  }

  protected convertDateFromClient(classStudent: IClassStudent): IClassStudent {
    return Object.assign({}, classStudent, {
      dateOfBirth: classStudent.dateOfBirth?.isValid() ? classStudent.dateOfBirth.toJSON() : undefined,
      startDate: classStudent.startDate?.isValid() ? classStudent.startDate.format(DATE_FORMAT) : undefined,
      admissionDate: classStudent.admissionDate?.isValid() ? classStudent.admissionDate.format(DATE_FORMAT) : undefined,
      endDate: classStudent.endDate?.isValid() ? classStudent.endDate.format(DATE_FORMAT) : undefined,
      createDate: classStudent.createDate?.isValid() ? classStudent.createDate.format(DATE_FORMAT) : undefined,
      lastModified: classStudent.lastModified?.isValid() ? classStudent.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: classStudent.cancelDate?.isValid() ? classStudent.cancelDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? dayjs(res.body.dateOfBirth) : undefined;
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.admissionDate = res.body.admissionDate ? dayjs(res.body.admissionDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
      res.body.cancelDate = res.body.cancelDate ? dayjs(res.body.cancelDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((classStudent: IClassStudent) => {
        classStudent.dateOfBirth = classStudent.dateOfBirth ? dayjs(classStudent.dateOfBirth) : undefined;
        classStudent.startDate = classStudent.startDate ? dayjs(classStudent.startDate) : undefined;
        classStudent.admissionDate = classStudent.admissionDate ? dayjs(classStudent.admissionDate) : undefined;
        classStudent.endDate = classStudent.endDate ? dayjs(classStudent.endDate) : undefined;
        classStudent.createDate = classStudent.createDate ? dayjs(classStudent.createDate) : undefined;
        classStudent.lastModified = classStudent.lastModified ? dayjs(classStudent.lastModified) : undefined;
        classStudent.cancelDate = classStudent.cancelDate ? dayjs(classStudent.cancelDate) : undefined;
      });
    }
    return res;
  }
}
