import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISchoolUser, SchoolUser } from '../school-user.model';
import { SchoolUserService } from '../service/school-user.service';

@Injectable({ providedIn: 'root' })
export class SchoolUserRoutingResolveService implements Resolve<ISchoolUser> {
  constructor(protected service: SchoolUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISchoolUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((schoolUser: HttpResponse<SchoolUser>) => {
          if (schoolUser.body) {
            return of(schoolUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SchoolUser());
  }
}
