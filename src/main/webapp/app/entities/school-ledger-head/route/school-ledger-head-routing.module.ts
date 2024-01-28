import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SchoolLedgerHeadComponent } from '../list/school-ledger-head.component';
import { SchoolLedgerHeadDetailComponent } from '../detail/school-ledger-head-detail.component';
import { SchoolLedgerHeadUpdateComponent } from '../update/school-ledger-head-update.component';
import { SchoolLedgerHeadRoutingResolveService } from './school-ledger-head-routing-resolve.service';

const schoolLedgerHeadRoute: Routes = [
  {
    path: '',
    component: SchoolLedgerHeadComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SchoolLedgerHeadDetailComponent,
    resolve: {
      schoolLedgerHead: SchoolLedgerHeadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SchoolLedgerHeadUpdateComponent,
    resolve: {
      schoolLedgerHead: SchoolLedgerHeadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SchoolLedgerHeadUpdateComponent,
    resolve: {
      schoolLedgerHead: SchoolLedgerHeadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(schoolLedgerHeadRoute)],
  exports: [RouterModule],
})
export class SchoolLedgerHeadRoutingModule {}
