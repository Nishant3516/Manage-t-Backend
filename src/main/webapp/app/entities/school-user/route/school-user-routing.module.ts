import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SchoolUserComponent } from '../list/school-user.component';
import { SchoolUserDetailComponent } from '../detail/school-user-detail.component';
import { SchoolUserUpdateComponent } from '../update/school-user-update.component';
import { SchoolUserRoutingResolveService } from './school-user-routing-resolve.service';

const schoolUserRoute: Routes = [
  {
    path: '',
    component: SchoolUserComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SchoolUserDetailComponent,
    resolve: {
      schoolUser: SchoolUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SchoolUserUpdateComponent,
    resolve: {
      schoolUser: SchoolUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SchoolUserUpdateComponent,
    resolve: {
      schoolUser: SchoolUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(schoolUserRoute)],
  exports: [RouterModule],
})
export class SchoolUserRoutingModule {}
