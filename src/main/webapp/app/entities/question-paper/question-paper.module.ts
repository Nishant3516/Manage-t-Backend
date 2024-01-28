import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { QuestionPaperComponent } from './list/question-paper.component';
import { QuestionPaperDetailComponent } from './detail/question-paper-detail.component';
import { QuestionPaperUpdateComponent } from './update/question-paper-update.component';
import { QuestionPaperDeleteDialogComponent } from './delete/question-paper-delete-dialog.component';
import { QuestionPaperRoutingModule } from './route/question-paper-routing.module';

@NgModule({
  imports: [SharedModule, QuestionPaperRoutingModule],
  declarations: [QuestionPaperComponent, QuestionPaperDetailComponent, QuestionPaperUpdateComponent, QuestionPaperDeleteDialogComponent],
  entryComponents: [QuestionPaperDeleteDialogComponent],
})
export class QuestionPaperModule {}
