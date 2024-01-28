import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAuditLog, getAuditLogIdentifier } from '../audit-log.model';

export type EntityResponseType = HttpResponse<IAuditLog>;
export type EntityArrayResponseType = HttpResponse<IAuditLog[]>;

@Injectable({ providedIn: 'root' })
export class AuditLogService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/audit-logs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(auditLog: IAuditLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(auditLog);
    return this.http
      .post<IAuditLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(auditLog: IAuditLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(auditLog);
    return this.http
      .put<IAuditLog>(`${this.resourceUrl}/${getAuditLogIdentifier(auditLog) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(auditLog: IAuditLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(auditLog);
    return this.http
      .patch<IAuditLog>(`${this.resourceUrl}/${getAuditLogIdentifier(auditLog) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAuditLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAuditLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAuditLogToCollectionIfMissing(auditLogCollection: IAuditLog[], ...auditLogsToCheck: (IAuditLog | null | undefined)[]): IAuditLog[] {
    const auditLogs: IAuditLog[] = auditLogsToCheck.filter(isPresent);
    if (auditLogs.length > 0) {
      const auditLogCollectionIdentifiers = auditLogCollection.map(auditLogItem => getAuditLogIdentifier(auditLogItem)!);
      const auditLogsToAdd = auditLogs.filter(auditLogItem => {
        const auditLogIdentifier = getAuditLogIdentifier(auditLogItem);
        if (auditLogIdentifier == null || auditLogCollectionIdentifiers.includes(auditLogIdentifier)) {
          return false;
        }
        auditLogCollectionIdentifiers.push(auditLogIdentifier);
        return true;
      });
      return [...auditLogsToAdd, ...auditLogCollection];
    }
    return auditLogCollection;
  }

  protected convertDateFromClient(auditLog: IAuditLog): IAuditLog {
    return Object.assign({}, auditLog, {
      createDate: auditLog.createDate?.isValid() ? auditLog.createDate.format(DATE_FORMAT) : undefined,
      lastModified: auditLog.lastModified?.isValid() ? auditLog.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: auditLog.cancelDate?.isValid() ? auditLog.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((auditLog: IAuditLog) => {
        auditLog.createDate = auditLog.createDate ? dayjs(auditLog.createDate) : undefined;
        auditLog.lastModified = auditLog.lastModified ? dayjs(auditLog.lastModified) : undefined;
        auditLog.cancelDate = auditLog.cancelDate ? dayjs(auditLog.cancelDate) : undefined;
      });
    }
    return res;
  }
}
