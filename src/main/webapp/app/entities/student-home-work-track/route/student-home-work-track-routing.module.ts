import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudentHomeWorkTrackComponent } from '../list/student-home-work-track.component';
import { StudentHomeWorkTrackDetailComponent } from '../detail/student-home-work-track-detail.component';
import { StudentHomeWorkTrackUpdateComponent } from '../update/student-home-work-track-update.component';
import { StudentHomeWorkTrackRoutingResolveService } from './student-home-work-track-routing-resolve.service';

const studentHomeWorkTrackRoute: Routes = [
  {
    path: '',
    component: StudentHomeWorkTrackComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StudentHomeWorkTrackDetailComponent,
    resolve: {
      studentHomeWorkTrack: StudentHomeWorkTrackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StudentHomeWorkTrackUpdateComponent,
    resolve: {
      studentHomeWorkTrack: StudentHomeWorkTrackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StudentHomeWorkTrackUpdateComponent,
    resolve: {
      studentHomeWorkTrack: StudentHomeWorkTrackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(studentHomeWorkTrackRoute)],
  exports: [RouterModule],
})
export class StudentHomeWorkTrackRoutingModule {}
