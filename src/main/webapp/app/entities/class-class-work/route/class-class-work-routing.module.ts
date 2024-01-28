import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClassClassWorkComponent } from '../list/class-class-work.component';
import { ClassClassWorkDetailComponent } from '../detail/class-class-work-detail.component';
import { ClassClassWorkUpdateComponent } from '../update/class-class-work-update.component';
import { ClassClassWorkRoutingResolveService } from './class-class-work-routing-resolve.service';

const classClassWorkRoute: Routes = [
  {
    path: '',
    component: ClassClassWorkComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassClassWorkDetailComponent,
    resolve: {
      classClassWork: ClassClassWorkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassClassWorkUpdateComponent,
    resolve: {
      classClassWork: ClassClassWorkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassClassWorkUpdateComponent,
    resolve: {
      classClassWork: ClassClassWorkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(classClassWorkRoute)],
  exports: [RouterModule],
})
export class ClassClassWorkRoutingModule {}
