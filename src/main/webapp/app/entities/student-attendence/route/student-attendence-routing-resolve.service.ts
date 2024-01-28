import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStudentAttendence, StudentAttendence } from '../student-attendence.model';
import { StudentAttendenceService } from '../service/student-attendence.service';

@Injectable({ providedIn: 'root' })
export class StudentAttendenceRoutingResolveService implements Resolve<IStudentAttendence> {
  constructor(protected service: StudentAttendenceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudentAttendence> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((studentAttendence: HttpResponse<StudentAttendence>) => {
          if (studentAttendence.body) {
            return of(studentAttendence.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StudentAttendence());
  }
}
