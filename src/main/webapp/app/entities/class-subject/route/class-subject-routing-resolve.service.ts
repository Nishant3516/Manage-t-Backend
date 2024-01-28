import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassSubject, ClassSubject } from '../class-subject.model';
import { ClassSubjectService } from '../service/class-subject.service';

@Injectable({ providedIn: 'root' })
export class ClassSubjectRoutingResolveService implements Resolve<IClassSubject> {
  constructor(protected service: ClassSubjectService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassSubject> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((classSubject: HttpResponse<ClassSubject>) => {
          if (classSubject.body) {
            return of(classSubject.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClassSubject());
  }
}
