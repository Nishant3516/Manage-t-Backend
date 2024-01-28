import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISchoolClass, SchoolClass } from '../school-class.model';
import { SchoolClassService } from '../service/school-class.service';

@Injectable({ providedIn: 'root' })
export class SchoolClassRoutingResolveService implements Resolve<ISchoolClass> {
  constructor(protected service: SchoolClassService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISchoolClass> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((schoolClass: HttpResponse<SchoolClass>) => {
          if (schoolClass.body) {
            return of(schoolClass.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SchoolClass());
  }
}
