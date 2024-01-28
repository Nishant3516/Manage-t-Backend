import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { STRouteComponent } from '../list/st-route.component';
import { STRouteDetailComponent } from '../detail/st-route-detail.component';
import { STRouteUpdateComponent } from '../update/st-route-update.component';
import { STRouteRoutingResolveService } from './st-route-routing-resolve.service';

const sTRouteRoute: Routes = [
  {
    path: '',
    component: STRouteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: STRouteDetailComponent,
    resolve: {
      sTRoute: STRouteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: STRouteUpdateComponent,
    resolve: {
      sTRoute: STRouteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: STRouteUpdateComponent,
    resolve: {
      sTRoute: STRouteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sTRouteRoute)],
  exports: [RouterModule],
})
export class STRouteRoutingModule {}
