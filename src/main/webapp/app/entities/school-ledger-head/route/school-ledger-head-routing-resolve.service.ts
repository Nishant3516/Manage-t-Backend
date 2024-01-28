import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISchoolLedgerHead, SchoolLedgerHead } from '../school-ledger-head.model';
import { SchoolLedgerHeadService } from '../service/school-ledger-head.service';

@Injectable({ providedIn: 'root' })
export class SchoolLedgerHeadRoutingResolveService implements Resolve<ISchoolLedgerHead> {
  constructor(protected service: SchoolLedgerHeadService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISchoolLedgerHead> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((schoolLedgerHead: HttpResponse<SchoolLedgerHead>) => {
          if (schoolLedgerHead.body) {
            return of(schoolLedgerHead.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SchoolLedgerHead());
  }
}
