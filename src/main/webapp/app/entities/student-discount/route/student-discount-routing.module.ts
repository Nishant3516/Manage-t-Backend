import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudentDiscountComponent } from '../list/student-discount.component';
import { StudentDiscountDetailComponent } from '../detail/student-discount-detail.component';
import { StudentDiscountUpdateComponent } from '../update/student-discount-update.component';
import { StudentDiscountRoutingResolveService } from './student-discount-routing-resolve.service';

const studentDiscountRoute: Routes = [
  {
    path: '',
    component: StudentDiscountComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StudentDiscountDetailComponent,
    resolve: {
      studentDiscount: StudentDiscountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StudentDiscountUpdateComponent,
    resolve: {
      studentDiscount: StudentDiscountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StudentDiscountUpdateComponent,
    resolve: {
      studentDiscount: StudentDiscountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(studentDiscountRoute)],
  exports: [RouterModule],
})
export class StudentDiscountRoutingModule {}
