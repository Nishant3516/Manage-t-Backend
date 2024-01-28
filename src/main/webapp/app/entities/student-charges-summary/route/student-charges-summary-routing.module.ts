import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudentChargesSummaryComponent } from '../list/student-charges-summary.component';
import { StudentChargesSummaryDetailComponent } from '../detail/student-charges-summary-detail.component';
import { StudentChargesSummaryUpdateComponent } from '../update/student-charges-summary-update.component';
import { StudentChargesSummaryRoutingResolveService } from './student-charges-summary-routing-resolve.service';

const studentChargesSummaryRoute: Routes = [
  {
    path: '',
    component: StudentChargesSummaryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StudentChargesSummaryDetailComponent,
    resolve: {
      studentChargesSummary: StudentChargesSummaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StudentChargesSummaryUpdateComponent,
    resolve: {
      studentChargesSummary: StudentChargesSummaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StudentChargesSummaryUpdateComponent,
    resolve: {
      studentChargesSummary: StudentChargesSummaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(studentChargesSummaryRoute)],
  exports: [RouterModule],
})
export class StudentChargesSummaryRoutingModule {}
