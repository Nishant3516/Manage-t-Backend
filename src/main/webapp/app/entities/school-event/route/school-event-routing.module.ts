import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SchoolEventComponent } from '../list/school-event.component';
import { SchoolEventDetailComponent } from '../detail/school-event-detail.component';
import { SchoolEventUpdateComponent } from '../update/school-event-update.component';
import { SchoolEventRoutingResolveService } from './school-event-routing-resolve.service';

const schoolEventRoute: Routes = [
  {
    path: '',
    component: SchoolEventComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SchoolEventDetailComponent,
    resolve: {
      schoolEvent: SchoolEventRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SchoolEventUpdateComponent,
    resolve: {
      schoolEvent: SchoolEventRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SchoolEventUpdateComponent,
    resolve: {
      schoolEvent: SchoolEventRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(schoolEventRoute)],
  exports: [RouterModule],
})
export class SchoolEventRoutingModule {}
