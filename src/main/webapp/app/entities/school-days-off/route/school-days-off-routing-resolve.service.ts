import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISchoolDaysOff, SchoolDaysOff } from '../school-days-off.model';
import { SchoolDaysOffService } from '../service/school-days-off.service';

@Injectable({ providedIn: 'root' })
export class SchoolDaysOffRoutingResolveService implements Resolve<ISchoolDaysOff> {
  constructor(protected service: SchoolDaysOffService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISchoolDaysOff> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((schoolDaysOff: HttpResponse<SchoolDaysOff>) => {
          if (schoolDaysOff.body) {
            return of(schoolDaysOff.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SchoolDaysOff());
  }
}
