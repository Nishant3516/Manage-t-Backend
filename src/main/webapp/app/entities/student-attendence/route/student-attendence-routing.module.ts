import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudentAttendenceComponent } from '../list/student-attendence.component';
import { StudentAttendenceDetailComponent } from '../detail/student-attendence-detail.component';
import { StudentAttendenceUpdateComponent } from '../update/student-attendence-update.component';
import { StudentAttendenceRoutingResolveService } from './student-attendence-routing-resolve.service';

const studentAttendenceRoute: Routes = [
  {
    path: '',
    component: StudentAttendenceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StudentAttendenceDetailComponent,
    resolve: {
      studentAttendence: StudentAttendenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StudentAttendenceUpdateComponent,
    resolve: {
      studentAttendence: StudentAttendenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StudentAttendenceUpdateComponent,
    resolve: {
      studentAttendence: StudentAttendenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(studentAttendenceRoute)],
  exports: [RouterModule],
})
export class StudentAttendenceRoutingModule {}
