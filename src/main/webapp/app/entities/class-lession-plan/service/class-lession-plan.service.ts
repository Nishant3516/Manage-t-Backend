import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassLessionPlan, getClassLessionPlanIdentifier } from '../class-lession-plan.model';

export type EntityResponseType = HttpResponse<IClassLessionPlan>;
export type EntityArrayResponseType = HttpResponse<IClassLessionPlan[]>;

@Injectable({ providedIn: 'root' })
export class ClassLessionPlanService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/class-lession-plans');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(classLessionPlan: IClassLessionPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classLessionPlan);
    return this.http
      .post<IClassLessionPlan>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(classLessionPlan: IClassLessionPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classLessionPlan);
    return this.http
      .put<IClassLessionPlan>(`${this.resourceUrl}/${getClassLessionPlanIdentifier(classLessionPlan) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(classLessionPlan: IClassLessionPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classLessionPlan);
    return this.http
      .patch<IClassLessionPlan>(`${this.resourceUrl}/${getClassLessionPlanIdentifier(classLessionPlan) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IClassLessionPlan>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClassLessionPlan[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClassLessionPlanToCollectionIfMissing(
    classLessionPlanCollection: IClassLessionPlan[],
    ...classLessionPlansToCheck: (IClassLessionPlan | null | undefined)[]
  ): IClassLessionPlan[] {
    const classLessionPlans: IClassLessionPlan[] = classLessionPlansToCheck.filter(isPresent);
    if (classLessionPlans.length > 0) {
      const classLessionPlanCollectionIdentifiers = classLessionPlanCollection.map(
        classLessionPlanItem => getClassLessionPlanIdentifier(classLessionPlanItem)!
      );
      const classLessionPlansToAdd = classLessionPlans.filter(classLessionPlanItem => {
        const classLessionPlanIdentifier = getClassLessionPlanIdentifier(classLessionPlanItem);
        if (classLessionPlanIdentifier == null || classLessionPlanCollectionIdentifiers.includes(classLessionPlanIdentifier)) {
          return false;
        }
        classLessionPlanCollectionIdentifiers.push(classLessionPlanIdentifier);
        return true;
      });
      return [...classLessionPlansToAdd, ...classLessionPlanCollection];
    }
    return classLessionPlanCollection;
  }

  protected convertDateFromClient(classLessionPlan: IClassLessionPlan): IClassLessionPlan {
    return Object.assign({}, classLessionPlan, {
      schoolDate: classLessionPlan.schoolDate?.isValid() ? classLessionPlan.schoolDate.format(DATE_FORMAT) : undefined,
      createDate: classLessionPlan.createDate?.isValid() ? classLessionPlan.createDate.format(DATE_FORMAT) : undefined,
      lastModified: classLessionPlan.lastModified?.isValid() ? classLessionPlan.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: classLessionPlan.cancelDate?.isValid() ? classLessionPlan.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((classLessionPlan: IClassLessionPlan) => {
        classLessionPlan.schoolDate = classLessionPlan.schoolDate ? dayjs(classLessionPlan.schoolDate) : undefined;
        classLessionPlan.createDate = classLessionPlan.createDate ? dayjs(classLessionPlan.createDate) : undefined;
        classLessionPlan.lastModified = classLessionPlan.lastModified ? dayjs(classLessionPlan.lastModified) : undefined;
        classLessionPlan.cancelDate = classLessionPlan.cancelDate ? dayjs(classLessionPlan.cancelDate) : undefined;
      });
    }
    return res;
  }
}
