import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudentAdditionalChargesComponent } from '../list/student-additional-charges.component';
import { StudentAdditionalChargesDetailComponent } from '../detail/student-additional-charges-detail.component';
import { StudentAdditionalChargesUpdateComponent } from '../update/student-additional-charges-update.component';
import { StudentAdditionalChargesRoutingResolveService } from './student-additional-charges-routing-resolve.service';

const studentAdditionalChargesRoute: Routes = [
  {
    path: '',
    component: StudentAdditionalChargesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StudentAdditionalChargesDetailComponent,
    resolve: {
      studentAdditionalCharges: StudentAdditionalChargesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StudentAdditionalChargesUpdateComponent,
    resolve: {
      studentAdditionalCharges: StudentAdditionalChargesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StudentAdditionalChargesUpdateComponent,
    resolve: {
      studentAdditionalCharges: StudentAdditionalChargesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(studentAdditionalChargesRoute)],
  exports: [RouterModule],
})
export class StudentAdditionalChargesRoutingModule {}
