import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIdStore, IdStore } from '../id-store.model';
import { IdStoreService } from '../service/id-store.service';

@Injectable({ providedIn: 'root' })
export class IdStoreRoutingResolveService implements Resolve<IIdStore> {
  constructor(protected service: IdStoreService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIdStore> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((idStore: HttpResponse<IdStore>) => {
          if (idStore.body) {
            return of(idStore.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IdStore());
  }
}
