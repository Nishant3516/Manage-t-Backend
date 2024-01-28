import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISchoolEvent, SchoolEvent } from '../school-event.model';
import { SchoolEventService } from '../service/school-event.service';

@Injectable({ providedIn: 'root' })
export class SchoolEventRoutingResolveService implements Resolve<ISchoolEvent> {
  constructor(protected service: SchoolEventService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISchoolEvent> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((schoolEvent: HttpResponse<SchoolEvent>) => {
          if (schoolEvent.body) {
            return of(schoolEvent.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SchoolEvent());
  }
}
