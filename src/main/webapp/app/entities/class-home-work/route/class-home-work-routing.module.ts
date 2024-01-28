import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClassHomeWorkComponent } from '../list/class-home-work.component';
import { ClassHomeWorkDetailComponent } from '../detail/class-home-work-detail.component';
import { ClassHomeWorkUpdateComponent } from '../update/class-home-work-update.component';
import { ClassHomeWorkRoutingResolveService } from './class-home-work-routing-resolve.service';

const classHomeWorkRoute: Routes = [
  {
    path: '',
    component: ClassHomeWorkComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassHomeWorkDetailComponent,
    resolve: {
      classHomeWork: ClassHomeWorkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassHomeWorkUpdateComponent,
    resolve: {
      classHomeWork: ClassHomeWorkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassHomeWorkUpdateComponent,
    resolve: {
      classHomeWork: ClassHomeWorkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(classHomeWorkRoute)],
  exports: [RouterModule],
})
export class ClassHomeWorkRoutingModule {}
