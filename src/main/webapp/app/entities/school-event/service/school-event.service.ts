import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchoolEvent, getSchoolEventIdentifier } from '../school-event.model';

export type EntityResponseType = HttpResponse<ISchoolEvent>;
export type EntityArrayResponseType = HttpResponse<ISchoolEvent[]>;

@Injectable({ providedIn: 'root' })
export class SchoolEventService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/school-events');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(schoolEvent: ISchoolEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolEvent);
    return this.http
      .post<ISchoolEvent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(schoolEvent: ISchoolEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolEvent);
    return this.http
      .put<ISchoolEvent>(`${this.resourceUrl}/${getSchoolEventIdentifier(schoolEvent) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(schoolEvent: ISchoolEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schoolEvent);
    return this.http
      .patch<ISchoolEvent>(`${this.resourceUrl}/${getSchoolEventIdentifier(schoolEvent) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISchoolEvent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISchoolEvent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSchoolEventToCollectionIfMissing(
    schoolEventCollection: ISchoolEvent[],
    ...schoolEventsToCheck: (ISchoolEvent | null | undefined)[]
  ): ISchoolEvent[] {
    const schoolEvents: ISchoolEvent[] = schoolEventsToCheck.filter(isPresent);
    if (schoolEvents.length > 0) {
      const schoolEventCollectionIdentifiers = schoolEventCollection.map(schoolEventItem => getSchoolEventIdentifier(schoolEventItem)!);
      const schoolEventsToAdd = schoolEvents.filter(schoolEventItem => {
        const schoolEventIdentifier = getSchoolEventIdentifier(schoolEventItem);
        if (schoolEventIdentifier == null || schoolEventCollectionIdentifiers.includes(schoolEventIdentifier)) {
          return false;
        }
        schoolEventCollectionIdentifiers.push(schoolEventIdentifier);
        return true;
      });
      return [...schoolEventsToAdd, ...schoolEventCollection];
    }
    return schoolEventCollection;
  }

  protected convertDateFromClient(schoolEvent: ISchoolEvent): ISchoolEvent {
    return Object.assign({}, schoolEvent, {
      startDate: schoolEvent.startDate?.isValid() ? schoolEvent.startDate.format(DATE_FORMAT) : undefined,
      endDate: schoolEvent.endDate?.isValid() ? schoolEvent.endDate.format(DATE_FORMAT) : undefined,
      createDate: schoolEvent.createDate?.isValid() ? schoolEvent.createDate.format(DATE_FORMAT) : undefined,
      lastModified: schoolEvent.lastModified?.isValid() ? schoolEvent.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: schoolEvent.cancelDate?.isValid() ? schoolEvent.cancelDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
      res.body.cancelDate = res.body.cancelDate ? dayjs(res.body.cancelDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((schoolEvent: ISchoolEvent) => {
        schoolEvent.startDate = schoolEvent.startDate ? dayjs(schoolEvent.startDate) : undefined;
        schoolEvent.endDate = schoolEvent.endDate ? dayjs(schoolEvent.endDate) : undefined;
        schoolEvent.createDate = schoolEvent.createDate ? dayjs(schoolEvent.createDate) : undefined;
        schoolEvent.lastModified = schoolEvent.lastModified ? dayjs(schoolEvent.lastModified) : undefined;
        schoolEvent.cancelDate = schoolEvent.cancelDate ? dayjs(schoolEvent.cancelDate) : undefined;
      });
    }
    return res;
  }
}
