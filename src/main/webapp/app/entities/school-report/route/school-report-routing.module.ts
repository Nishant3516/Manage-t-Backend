import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SchoolReportComponent } from '../list/school-report.component';
import { SchoolReportDetailComponent } from '../detail/school-report-detail.component';
import { SchoolReportUpdateComponent } from '../update/school-report-update.component';
import { SchoolReportRoutingResolveService } from './school-report-routing-resolve.service';

const schoolReportRoute: Routes = [
  {
    path: '',
    component: SchoolReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SchoolReportDetailComponent,
    resolve: {
      schoolReport: SchoolReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SchoolReportUpdateComponent,
    resolve: {
      schoolReport: SchoolReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SchoolReportUpdateComponent,
    resolve: {
      schoolReport: SchoolReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(schoolReportRoute)],
  exports: [RouterModule],
})
export class SchoolReportRoutingModule {}
