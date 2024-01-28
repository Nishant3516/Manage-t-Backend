import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStudentPayments, StudentPayments } from '../student-payments.model';
import { StudentPaymentsService } from '../service/student-payments.service';

@Injectable({ providedIn: 'root' })
export class StudentPaymentsRoutingResolveService implements Resolve<IStudentPayments> {
  constructor(protected service: StudentPaymentsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudentPayments> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((studentPayments: HttpResponse<StudentPayments>) => {
          if (studentPayments.body) {
            return of(studentPayments.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StudentPayments());
  }
}
