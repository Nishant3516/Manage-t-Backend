import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SubjectChapterComponent } from './list/subject-chapter.component';
import { SubjectChapterDetailComponent } from './detail/subject-chapter-detail.component';
import { SubjectChapterUpdateComponent } from './update/subject-chapter-update.component';
import { SubjectChapterDeleteDialogComponent } from './delete/subject-chapter-delete-dialog.component';
import { SubjectChapterRoutingModule } from './route/subject-chapter-routing.module';

@NgModule({
  imports: [SharedModule, SubjectChapterRoutingModule],
  declarations: [
    SubjectChapterComponent,
    SubjectChapterDetailComponent,
    SubjectChapterUpdateComponent,
    SubjectChapterDeleteDialogComponent,
  ],
  entryComponents: [SubjectChapterDeleteDialogComponent],
})
export class SubjectChapterModule {}
