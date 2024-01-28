import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IdStoreComponent } from '../list/id-store.component';
import { IdStoreDetailComponent } from '../detail/id-store-detail.component';
import { IdStoreUpdateComponent } from '../update/id-store-update.component';
import { IdStoreRoutingResolveService } from './id-store-routing-resolve.service';

const idStoreRoute: Routes = [
  {
    path: '',
    component: IdStoreComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IdStoreDetailComponent,
    resolve: {
      idStore: IdStoreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IdStoreUpdateComponent,
    resolve: {
      idStore: IdStoreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IdStoreUpdateComponent,
    resolve: {
      idStore: IdStoreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(idStoreRoute)],
  exports: [RouterModule],
})
export class IdStoreRoutingModule {}
