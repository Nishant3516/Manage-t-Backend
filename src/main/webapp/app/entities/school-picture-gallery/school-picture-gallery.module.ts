import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SchoolPictureGalleryComponent } from './list/school-picture-gallery.component';
import { SchoolPictureGalleryDetailComponent } from './detail/school-picture-gallery-detail.component';
import { SchoolPictureGalleryUpdateComponent } from './update/school-picture-gallery-update.component';
import { SchoolPictureGalleryDeleteDialogComponent } from './delete/school-picture-gallery-delete-dialog.component';
import { SchoolPictureGalleryRoutingModule } from './route/school-picture-gallery-routing.module';

@NgModule({
  imports: [SharedModule, SchoolPictureGalleryRoutingModule],
  declarations: [
    SchoolPictureGalleryComponent,
    SchoolPictureGalleryDetailComponent,
    SchoolPictureGalleryUpdateComponent,
    SchoolPictureGalleryDeleteDialogComponent,
  ],
  entryComponents: [SchoolPictureGalleryDeleteDialogComponent],
})
export class SchoolPictureGalleryModule {}
