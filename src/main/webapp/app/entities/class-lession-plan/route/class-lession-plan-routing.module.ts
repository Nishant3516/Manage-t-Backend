import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClassLessionPlanComponent } from '../list/class-lession-plan.component';
import { ClassLessionPlanDetailComponent } from '../detail/class-lession-plan-detail.component';
import { ClassLessionPlanUpdateComponent } from '../update/class-lession-plan-update.component';
import { ClassLessionPlanRoutingResolveService } from './class-lession-plan-routing-resolve.service';

const classLessionPlanRoute: Routes = [
  {
    path: '',
    component: ClassLessionPlanComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassLessionPlanDetailComponent,
    resolve: {
      classLessionPlan: ClassLessionPlanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassLessionPlanUpdateComponent,
    resolve: {
      classLessionPlan: ClassLessionPlanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassLessionPlanUpdateComponent,
    resolve: {
      classLessionPlan: ClassLessionPlanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(classLessionPlanRoute)],
  exports: [RouterModule],
})
export class ClassLessionPlanRoutingModule {}
