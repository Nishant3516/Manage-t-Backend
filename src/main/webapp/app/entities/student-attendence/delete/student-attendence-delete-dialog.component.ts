import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentAttendence } from '../student-attendence.model';
import { StudentAttendenceService } from '../service/student-attendence.service';

@Component({
  templateUrl: './student-attendence-delete-dialog.component.html',
})
export class StudentAttendenceDeleteDialogComponent {
  studentAttendence?: IStudentAttendence;

  constructor(protected studentAttendenceService: StudentAttendenceService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentAttendenceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
