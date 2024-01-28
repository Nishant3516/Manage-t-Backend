import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISchoolNotifications, SchoolNotifications } from '../school-notifications.model';
import { SchoolNotificationsService } from '../service/school-notifications.service';

@Injectable({ providedIn: 'root' })
export class SchoolNotificationsRoutingResolveService implements Resolve<ISchoolNotifications> {
  constructor(protected service: SchoolNotificationsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISchoolNotifications> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((schoolNotifications: HttpResponse<SchoolNotifications>) => {
          if (schoolNotifications.body) {
            return of(schoolNotifications.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SchoolNotifications());
  }
}
