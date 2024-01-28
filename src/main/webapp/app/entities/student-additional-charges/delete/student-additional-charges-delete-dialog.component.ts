import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentAdditionalCharges } from '../student-additional-charges.model';
import { StudentAdditionalChargesService } from '../service/student-additional-charges.service';

@Component({
  templateUrl: './student-additional-charges-delete-dialog.component.html',
})
export class StudentAdditionalChargesDeleteDialogComponent {
  studentAdditionalCharges?: IStudentAdditionalCharges;

  constructor(protected studentAdditionalChargesService: StudentAdditionalChargesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentAdditionalChargesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
