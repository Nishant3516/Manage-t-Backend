import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VendorsComponent } from '../list/vendors.component';
import { VendorsDetailComponent } from '../detail/vendors-detail.component';
import { VendorsUpdateComponent } from '../update/vendors-update.component';
import { VendorsRoutingResolveService } from './vendors-routing-resolve.service';

const vendorsRoute: Routes = [
  {
    path: '',
    component: VendorsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VendorsDetailComponent,
    resolve: {
      vendors: VendorsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VendorsUpdateComponent,
    resolve: {
      vendors: VendorsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VendorsUpdateComponent,
    resolve: {
      vendors: VendorsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vendorsRoute)],
  exports: [RouterModule],
})
export class VendorsRoutingModule {}
