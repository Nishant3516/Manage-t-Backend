import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISchoolReport, SchoolReport } from '../school-report.model';
import { SchoolReportService } from '../service/school-report.service';

@Injectable({ providedIn: 'root' })
export class SchoolReportRoutingResolveService implements Resolve<ISchoolReport> {
  constructor(protected service: SchoolReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISchoolReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((schoolReport: HttpResponse<SchoolReport>) => {
          if (schoolReport.body) {
            return of(schoolReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SchoolReport());
  }
}
