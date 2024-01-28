import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISTRoute, STRoute } from '../st-route.model';
import { STRouteService } from '../service/st-route.service';

@Injectable({ providedIn: 'root' })
export class STRouteRoutingResolveService implements Resolve<ISTRoute> {
  constructor(protected service: STRouteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISTRoute> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sTRoute: HttpResponse<STRoute>) => {
          if (sTRoute.body) {
            return of(sTRoute.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new STRoute());
  }
}
