import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClassSubjectComponent } from '../list/class-subject.component';
import { ClassSubjectDetailComponent } from '../detail/class-subject-detail.component';
import { ClassSubjectUpdateComponent } from '../update/class-subject-update.component';
import { ClassSubjectRoutingResolveService } from './class-subject-routing-resolve.service';

const classSubjectRoute: Routes = [
  {
    path: '',
    component: ClassSubjectComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassSubjectDetailComponent,
    resolve: {
      classSubject: ClassSubjectRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassSubjectUpdateComponent,
    resolve: {
      classSubject: ClassSubjectRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassSubjectUpdateComponent,
    resolve: {
      classSubject: ClassSubjectRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(classSubjectRoute)],
  exports: [RouterModule],
})
export class ClassSubjectRoutingModule {}
