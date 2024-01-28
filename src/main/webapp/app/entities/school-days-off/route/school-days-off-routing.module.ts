import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SchoolDaysOffComponent } from '../list/school-days-off.component';
import { SchoolDaysOffDetailComponent } from '../detail/school-days-off-detail.component';
import { SchoolDaysOffUpdateComponent } from '../update/school-days-off-update.component';
import { SchoolDaysOffRoutingResolveService } from './school-days-off-routing-resolve.service';

const schoolDaysOffRoute: Routes = [
  {
    path: '',
    component: SchoolDaysOffComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SchoolDaysOffDetailComponent,
    resolve: {
      schoolDaysOff: SchoolDaysOffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SchoolDaysOffUpdateComponent,
    resolve: {
      schoolDaysOff: SchoolDaysOffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SchoolDaysOffUpdateComponent,
    resolve: {
      schoolDaysOff: SchoolDaysOffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(schoolDaysOffRoute)],
  exports: [RouterModule],
})
export class SchoolDaysOffRoutingModule {}
