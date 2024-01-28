import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassLessionPlanTrack, ClassLessionPlanTrack } from '../class-lession-plan-track.model';
import { ClassLessionPlanTrackService } from '../service/class-lession-plan-track.service';

@Injectable({ providedIn: 'root' })
export class ClassLessionPlanTrackRoutingResolveService implements Resolve<IClassLessionPlanTrack> {
  constructor(protected service: ClassLessionPlanTrackService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassLessionPlanTrack> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((classLessionPlanTrack: HttpResponse<ClassLessionPlanTrack>) => {
          if (classLessionPlanTrack.body) {
            return of(classLessionPlanTrack.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClassLessionPlanTrack());
  }
}
