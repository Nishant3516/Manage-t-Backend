import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStudentDiscount, StudentDiscount } from '../student-discount.model';
import { StudentDiscountService } from '../service/student-discount.service';

@Injectable({ providedIn: 'root' })
export class StudentDiscountRoutingResolveService implements Resolve<IStudentDiscount> {
  constructor(protected service: StudentDiscountService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudentDiscount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((studentDiscount: HttpResponse<StudentDiscount>) => {
          if (studentDiscount.body) {
            return of(studentDiscount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StudentDiscount());
  }
}
