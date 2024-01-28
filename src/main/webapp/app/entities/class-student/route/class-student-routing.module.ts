import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClassStudentComponent } from '../list/class-student.component';
import { ClassStudentDetailComponent } from '../detail/class-student-detail.component';
import { ClassStudentUpdateComponent } from '../update/class-student-update.component';
import { ClassStudentRoutingResolveService } from './class-student-routing-resolve.service';

const classStudentRoute: Routes = [
  {
    path: '',
    component: ClassStudentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassStudentDetailComponent,
    resolve: {
      classStudent: ClassStudentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassStudentUpdateComponent,
    resolve: {
      classStudent: ClassStudentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassStudentUpdateComponent,
    resolve: {
      classStudent: ClassStudentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(classStudentRoute)],
  exports: [RouterModule],
})
export class ClassStudentRoutingModule {}
