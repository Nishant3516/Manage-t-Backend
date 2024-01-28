import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuestionPaper } from '../question-paper.model';
import { QuestionPaperService } from '../service/question-paper.service';

@Component({
  templateUrl: './question-paper-delete-dialog.component.html',
})
export class QuestionPaperDeleteDialogComponent {
  questionPaper?: IQuestionPaper;

  constructor(protected questionPaperService: QuestionPaperService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.questionPaperService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
