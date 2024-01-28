import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudentPaymentsComponent } from '../list/student-payments.component';
import { StudentPaymentsDetailComponent } from '../detail/student-payments-detail.component';
import { StudentPaymentsUpdateComponent } from '../update/student-payments-update.component';
import { StudentPaymentsRoutingResolveService } from './student-payments-routing-resolve.service';

const studentPaymentsRoute: Routes = [
  {
    path: '',
    component: StudentPaymentsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StudentPaymentsDetailComponent,
    resolve: {
      studentPayments: StudentPaymentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StudentPaymentsUpdateComponent,
    resolve: {
      studentPayments: StudentPaymentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StudentPaymentsUpdateComponent,
    resolve: {
      studentPayments: StudentPaymentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(studentPaymentsRoute)],
  exports: [RouterModule],
})
export class StudentPaymentsRoutingModule {}
