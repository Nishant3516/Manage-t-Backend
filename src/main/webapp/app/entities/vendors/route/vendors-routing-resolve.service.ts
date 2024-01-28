import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVendors, Vendors } from '../vendors.model';
import { VendorsService } from '../service/vendors.service';

@Injectable({ providedIn: 'root' })
export class VendorsRoutingResolveService implements Resolve<IVendors> {
  constructor(protected service: VendorsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVendors> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vendors: HttpResponse<Vendors>) => {
          if (vendors.body) {
            return of(vendors.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vendors());
  }
}
