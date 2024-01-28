import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ChapterSectionComponent } from './list/chapter-section.component';
import { ChapterSectionDetailComponent } from './detail/chapter-section-detail.component';
import { ChapterSectionUpdateComponent } from './update/chapter-section-update.component';
import { ChapterSectionDeleteDialogComponent } from './delete/chapter-section-delete-dialog.component';
import { ChapterSectionRoutingModule } from './route/chapter-section-routing.module';

@NgModule({
  imports: [SharedModule, ChapterSectionRoutingModule],
  declarations: [
    ChapterSectionComponent,
    ChapterSectionDetailComponent,
    ChapterSectionUpdateComponent,
    ChapterSectionDeleteDialogComponent,
  ],
  entryComponents: [ChapterSectionDeleteDialogComponent],
})
export class ChapterSectionModule {}
