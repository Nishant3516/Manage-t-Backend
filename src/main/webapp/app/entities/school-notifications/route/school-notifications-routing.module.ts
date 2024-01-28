import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SchoolNotificationsComponent } from '../list/school-notifications.component';
import { SchoolNotificationsDetailComponent } from '../detail/school-notifications-detail.component';
import { SchoolNotificationsUpdateComponent } from '../update/school-notifications-update.component';
import { SchoolNotificationsRoutingResolveService } from './school-notifications-routing-resolve.service';

const schoolNotificationsRoute: Routes = [
  {
    path: '',
    component: SchoolNotificationsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SchoolNotificationsDetailComponent,
    resolve: {
      schoolNotifications: SchoolNotificationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SchoolNotificationsUpdateComponent,
    resolve: {
      schoolNotifications: SchoolNotificationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SchoolNotificationsUpdateComponent,
    resolve: {
      schoolNotifications: SchoolNotificationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(schoolNotificationsRoute)],
  exports: [RouterModule],
})
export class SchoolNotificationsRoutingModule {}
