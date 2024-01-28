import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassStudent, ClassStudent } from '../class-student.model';
import { ClassStudentService } from '../service/class-student.service';

@Injectable({ providedIn: 'root' })
export class ClassStudentRoutingResolveService implements Resolve<IClassStudent> {
  constructor(protected service: ClassStudentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassStudent> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((classStudent: HttpResponse<ClassStudent>) => {
          if (classStudent.body) {
            return of(classStudent.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClassStudent());
  }
}
