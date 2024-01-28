import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChapterSection } from '../chapter-section.model';
import { ChapterSectionService } from '../service/chapter-section.service';

@Component({
  templateUrl: './chapter-section-delete-dialog.component.html',
})
export class ChapterSectionDeleteDialogComponent {
  chapterSection?: IChapterSection;

  constructor(protected chapterSectionService: ChapterSectionService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chapterSectionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
