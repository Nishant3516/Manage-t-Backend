import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClassFeeComponent } from '../list/class-fee.component';
import { ClassFeeDetailComponent } from '../detail/class-fee-detail.component';
import { ClassFeeUpdateComponent } from '../update/class-fee-update.component';
import { ClassFeeRoutingResolveService } from './class-fee-routing-resolve.service';

const classFeeRoute: Routes = [
  {
    path: '',
    component: ClassFeeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassFeeDetailComponent,
    resolve: {
      classFee: ClassFeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassFeeUpdateComponent,
    resolve: {
      classFee: ClassFeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassFeeUpdateComponent,
    resolve: {
      classFee: ClassFeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(classFeeRoute)],
  exports: [RouterModule],
})
export class ClassFeeRoutingModule {}
