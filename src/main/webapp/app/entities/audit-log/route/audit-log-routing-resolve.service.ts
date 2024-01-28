import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAuditLog, AuditLog } from '../audit-log.model';
import { AuditLogService } from '../service/audit-log.service';

@Injectable({ providedIn: 'root' })
export class AuditLogRoutingResolveService implements Resolve<IAuditLog> {
  constructor(protected service: AuditLogService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAuditLog> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((auditLog: HttpResponse<AuditLog>) => {
          if (auditLog.body) {
            return of(auditLog.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AuditLog());
  }
}
