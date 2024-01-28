import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SchoolClassComponent } from '../list/school-class.component';
import { SchoolClassDetailComponent } from '../detail/school-class-detail.component';
import { SchoolClassUpdateComponent } from '../update/school-class-update.component';
import { SchoolClassRoutingResolveService } from './school-class-routing-resolve.service';

const schoolClassRoute: Routes = [
  {
    path: '',
    component: SchoolClassComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SchoolClassDetailComponent,
    resolve: {
      schoolClass: SchoolClassRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SchoolClassUpdateComponent,
    resolve: {
      schoolClass: SchoolClassRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SchoolClassUpdateComponent,
    resolve: {
      schoolClass: SchoolClassRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(schoolClassRoute)],
  exports: [RouterModule],
})
export class SchoolClassRoutingModule {}
