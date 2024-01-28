import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentPayments } from '../student-payments.model';
import { StudentPaymentsService } from '../service/student-payments.service';

@Component({
  templateUrl: './student-payments-delete-dialog.component.html',
})
export class StudentPaymentsDeleteDialogComponent {
  studentPayments?: IStudentPayments;

  constructor(protected studentPaymentsService: StudentPaymentsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentPaymentsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
