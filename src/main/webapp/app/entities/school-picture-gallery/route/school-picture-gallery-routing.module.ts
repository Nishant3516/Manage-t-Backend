import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SchoolPictureGalleryComponent } from '../list/school-picture-gallery.component';
import { SchoolPictureGalleryDetailComponent } from '../detail/school-picture-gallery-detail.component';
import { SchoolPictureGalleryUpdateComponent } from '../update/school-picture-gallery-update.component';
import { SchoolPictureGalleryRoutingResolveService } from './school-picture-gallery-routing-resolve.service';

const schoolPictureGalleryRoute: Routes = [
  {
    path: '',
    component: SchoolPictureGalleryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SchoolPictureGalleryDetailComponent,
    resolve: {
      schoolPictureGallery: SchoolPictureGalleryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SchoolPictureGalleryUpdateComponent,
    resolve: {
      schoolPictureGallery: SchoolPictureGalleryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SchoolPictureGalleryUpdateComponent,
    resolve: {
      schoolPictureGallery: SchoolPictureGalleryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(schoolPictureGalleryRoute)],
  exports: [RouterModule],
})
export class SchoolPictureGalleryRoutingModule {}
