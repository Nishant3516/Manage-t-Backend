import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISTIncomeExpenses, STIncomeExpenses } from '../st-income-expenses.model';
import { STIncomeExpensesService } from '../service/st-income-expenses.service';

@Injectable({ providedIn: 'root' })
export class STIncomeExpensesRoutingResolveService implements Resolve<ISTIncomeExpenses> {
  constructor(protected service: STIncomeExpensesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISTIncomeExpenses> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sTIncomeExpenses: HttpResponse<STIncomeExpenses>) => {
          if (sTIncomeExpenses.body) {
            return of(sTIncomeExpenses.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new STIncomeExpenses());
  }
}
