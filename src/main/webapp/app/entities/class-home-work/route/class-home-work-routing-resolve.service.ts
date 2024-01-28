import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassHomeWork, ClassHomeWork } from '../class-home-work.model';
import { ClassHomeWorkService } from '../service/class-home-work.service';

@Injectable({ providedIn: 'root' })
export class ClassHomeWorkRoutingResolveService implements Resolve<IClassHomeWork> {
  constructor(protected service: ClassHomeWorkService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassHomeWork> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((classHomeWork: HttpResponse<ClassHomeWork>) => {
          if (classHomeWork.body) {
            return of(classHomeWork.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClassHomeWork());
  }
}
