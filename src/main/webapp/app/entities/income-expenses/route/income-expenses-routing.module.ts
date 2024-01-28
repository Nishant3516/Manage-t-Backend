import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IncomeExpensesComponent } from '../list/income-expenses.component';
import { IncomeExpensesDetailComponent } from '../detail/income-expenses-detail.component';
import { IncomeExpensesUpdateComponent } from '../update/income-expenses-update.component';
import { IncomeExpensesRoutingResolveService } from './income-expenses-routing-resolve.service';

const incomeExpensesRoute: Routes = [
  {
    path: '',
    component: IncomeExpensesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IncomeExpensesDetailComponent,
    resolve: {
      incomeExpenses: IncomeExpensesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IncomeExpensesUpdateComponent,
    resolve: {
      incomeExpenses: IncomeExpensesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IncomeExpensesUpdateComponent,
    resolve: {
      incomeExpenses: IncomeExpensesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(incomeExpensesRoute)],
  exports: [RouterModule],
})
export class IncomeExpensesRoutingModule {}
