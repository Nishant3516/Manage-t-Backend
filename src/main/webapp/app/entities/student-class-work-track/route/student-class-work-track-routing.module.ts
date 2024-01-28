import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudentClassWorkTrackComponent } from '../list/student-class-work-track.component';
import { StudentClassWorkTrackDetailComponent } from '../detail/student-class-work-track-detail.component';
import { StudentClassWorkTrackUpdateComponent } from '../update/student-class-work-track-update.component';
import { StudentClassWorkTrackRoutingResolveService } from './student-class-work-track-routing-resolve.service';

const studentClassWorkTrackRoute: Routes = [
  {
    path: '',
    component: StudentClassWorkTrackComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StudentClassWorkTrackDetailComponent,
    resolve: {
      studentClassWorkTrack: StudentClassWorkTrackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StudentClassWorkTrackUpdateComponent,
    resolve: {
      studentClassWorkTrack: StudentClassWorkTrackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StudentClassWorkTrackUpdateComponent,
    resolve: {
      studentClassWorkTrack: StudentClassWorkTrackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(studentClassWorkTrackRoute)],
  exports: [RouterModule],
})
export class StudentClassWorkTrackRoutingModule {}
