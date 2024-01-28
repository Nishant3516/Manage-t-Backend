import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIncomeExpenses, IncomeExpenses } from '../income-expenses.model';
import { IncomeExpensesService } from '../service/income-expenses.service';

@Injectable({ providedIn: 'root' })
export class IncomeExpensesRoutingResolveService implements Resolve<IIncomeExpenses> {
  constructor(protected service: IncomeExpensesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIncomeExpenses> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((incomeExpenses: HttpResponse<IncomeExpenses>) => {
          if (incomeExpenses.body) {
            return of(incomeExpenses.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IncomeExpenses());
  }
}
