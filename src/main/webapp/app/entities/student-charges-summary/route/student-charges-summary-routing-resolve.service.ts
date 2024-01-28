import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStudentChargesSummary, StudentChargesSummary } from '../student-charges-summary.model';
import { StudentChargesSummaryService } from '../service/student-charges-summary.service';

@Injectable({ providedIn: 'root' })
export class StudentChargesSummaryRoutingResolveService implements Resolve<IStudentChargesSummary> {
  constructor(protected service: StudentChargesSummaryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudentChargesSummary> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((studentChargesSummary: HttpResponse<StudentChargesSummary>) => {
          if (studentChargesSummary.body) {
            return of(studentChargesSummary.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StudentChargesSummary());
  }
}
