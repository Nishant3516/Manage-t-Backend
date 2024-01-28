import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClassLessionPlanTrackComponent } from '../list/class-lession-plan-track.component';
import { ClassLessionPlanTrackDetailComponent } from '../detail/class-lession-plan-track-detail.component';
import { ClassLessionPlanTrackUpdateComponent } from '../update/class-lession-plan-track-update.component';
import { ClassLessionPlanTrackRoutingResolveService } from './class-lession-plan-track-routing-resolve.service';

const classLessionPlanTrackRoute: Routes = [
  {
    path: '',
    component: ClassLessionPlanTrackComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassLessionPlanTrackDetailComponent,
    resolve: {
      classLessionPlanTrack: ClassLessionPlanTrackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassLessionPlanTrackUpdateComponent,
    resolve: {
      classLessionPlanTrack: ClassLessionPlanTrackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassLessionPlanTrackUpdateComponent,
    resolve: {
      classLessionPlanTrack: ClassLessionPlanTrackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(classLessionPlanTrackRoute)],
  exports: [RouterModule],
})
export class ClassLessionPlanTrackRoutingModule {}
