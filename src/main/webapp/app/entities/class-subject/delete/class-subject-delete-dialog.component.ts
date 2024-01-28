import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassSubject } from '../class-subject.model';
import { ClassSubjectService } from '../service/class-subject.service';

@Component({
  templateUrl: './class-subject-delete-dialog.component.html',
})
export class ClassSubjectDeleteDialogComponent {
  classSubject?: IClassSubject;

  constructor(protected classSubjectService: ClassSubjectService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classSubjectService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
