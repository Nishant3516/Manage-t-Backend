import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassStudent } from '../class-student.model';
import { ClassStudentService } from '../service/class-student.service';

@Component({
  templateUrl: './class-student-delete-dialog.component.html',
})
export class ClassStudentDeleteDialogComponent {
  classStudent?: IClassStudent;

  constructor(protected classStudentService: ClassStudentService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classStudentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
