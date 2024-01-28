import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassLessionPlan, ClassLessionPlan } from '../class-lession-plan.model';
import { ClassLessionPlanService } from '../service/class-lession-plan.service';

@Injectable({ providedIn: 'root' })
export class ClassLessionPlanRoutingResolveService implements Resolve<IClassLessionPlan> {
  constructor(protected service: ClassLessionPlanService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassLessionPlan> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((classLessionPlan: HttpResponse<ClassLessionPlan>) => {
          if (classLessionPlan.body) {
            return of(classLessionPlan.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClassLessionPlan());
  }
}
