import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SchoolVideoGalleryComponent } from '../list/school-video-gallery.component';
import { SchoolVideoGalleryDetailComponent } from '../detail/school-video-gallery-detail.component';
import { SchoolVideoGalleryUpdateComponent } from '../update/school-video-gallery-update.component';
import { SchoolVideoGalleryRoutingResolveService } from './school-video-gallery-routing-resolve.service';

const schoolVideoGalleryRoute: Routes = [
  {
    path: '',
    component: SchoolVideoGalleryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SchoolVideoGalleryDetailComponent,
    resolve: {
      schoolVideoGallery: SchoolVideoGalleryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SchoolVideoGalleryUpdateComponent,
    resolve: {
      schoolVideoGallery: SchoolVideoGalleryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SchoolVideoGalleryUpdateComponent,
    resolve: {
      schoolVideoGallery: SchoolVideoGalleryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(schoolVideoGalleryRoute)],
  exports: [RouterModule],
})
export class SchoolVideoGalleryRoutingModule {}
