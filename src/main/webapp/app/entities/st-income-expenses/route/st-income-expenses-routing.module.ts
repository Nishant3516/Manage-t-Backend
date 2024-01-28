import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { STIncomeExpensesComponent } from '../list/st-income-expenses.component';
import { STIncomeExpensesDetailComponent } from '../detail/st-income-expenses-detail.component';
import { STIncomeExpensesUpdateComponent } from '../update/st-income-expenses-update.component';
import { STIncomeExpensesRoutingResolveService } from './st-income-expenses-routing-resolve.service';

const sTIncomeExpensesRoute: Routes = [
  {
    path: '',
    component: STIncomeExpensesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: STIncomeExpensesDetailComponent,
    resolve: {
      sTIncomeExpenses: STIncomeExpensesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: STIncomeExpensesUpdateComponent,
    resolve: {
      sTIncomeExpenses: STIncomeExpensesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: STIncomeExpensesUpdateComponent,
    resolve: {
      sTIncomeExpenses: STIncomeExpensesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sTIncomeExpensesRoute)],
  exports: [RouterModule],
})
export class STIncomeExpensesRoutingModule {}
