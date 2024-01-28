import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISubjectChapter } from '../subject-chapter.model';
import { SubjectChapterService } from '../service/subject-chapter.service';

@Component({
  templateUrl: './subject-chapter-delete-dialog.component.html',
})
export class SubjectChapterDeleteDialogComponent {
  subjectChapter?: ISubjectChapter;

  constructor(protected subjectChapterService: SubjectChapterService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subjectChapterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
