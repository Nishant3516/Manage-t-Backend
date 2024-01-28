import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStudentAdditionalCharges, StudentAdditionalCharges } from '../student-additional-charges.model';
import { StudentAdditionalChargesService } from '../service/student-additional-charges.service';

@Injectable({ providedIn: 'root' })
export class StudentAdditionalChargesRoutingResolveService implements Resolve<IStudentAdditionalCharges> {
  constructor(protected service: StudentAdditionalChargesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudentAdditionalCharges> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((studentAdditionalCharges: HttpResponse<StudentAdditionalCharges>) => {
          if (studentAdditionalCharges.body) {
            return of(studentAdditionalCharges.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StudentAdditionalCharges());
  }
}
