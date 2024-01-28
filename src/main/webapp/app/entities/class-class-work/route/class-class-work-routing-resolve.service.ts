import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassClassWork, ClassClassWork } from '../class-class-work.model';
import { ClassClassWorkService } from '../service/class-class-work.service';

@Injectable({ providedIn: 'root' })
export class ClassClassWorkRoutingResolveService implements Resolve<IClassClassWork> {
  constructor(protected service: ClassClassWorkService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassClassWork> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((classClassWork: HttpResponse<ClassClassWork>) => {
          if (classClassWork.body) {
            return of(classClassWork.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClassClassWork());
  }
}
