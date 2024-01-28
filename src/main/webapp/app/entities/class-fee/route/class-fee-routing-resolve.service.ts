import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassFee, ClassFee } from '../class-fee.model';
import { ClassFeeService } from '../service/class-fee.service';

@Injectable({ providedIn: 'root' })
export class ClassFeeRoutingResolveService implements Resolve<IClassFee> {
  constructor(protected service: ClassFeeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassFee> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((classFee: HttpResponse<ClassFee>) => {
          if (classFee.body) {
            return of(classFee.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClassFee());
  }
}
