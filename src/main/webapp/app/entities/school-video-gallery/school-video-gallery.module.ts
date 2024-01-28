import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SchoolVideoGalleryComponent } from './list/school-video-gallery.component';
import { SchoolVideoGalleryDetailComponent } from './detail/school-video-gallery-detail.component';
import { SchoolVideoGalleryUpdateComponent } from './update/school-video-gallery-update.component';
import { SchoolVideoGalleryDeleteDialogComponent } from './delete/school-video-gallery-delete-dialog.component';
import { SchoolVideoGalleryRoutingModule } from './route/school-video-gallery-routing.module';

@NgModule({
  imports: [SharedModule, SchoolVideoGalleryRoutingModule],
  declarations: [
    SchoolVideoGalleryComponent,
    SchoolVideoGalleryDetailComponent,
    SchoolVideoGalleryUpdateComponent,
    SchoolVideoGalleryDeleteDialogComponent,
  ],
  entryComponents: [SchoolVideoGalleryDeleteDialogComponent],
})
export class SchoolVideoGalleryModule {}
