import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentDiscount } from '../student-discount.model';
import { StudentDiscountService } from '../service/student-discount.service';

@Component({
  templateUrl: './student-discount-delete-dialog.component.html',
})
export class StudentDiscountDeleteDialogComponent {
  studentDiscount?: IStudentDiscount;

  constructor(protected studentDiscountService: StudentDiscountService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentDiscountService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
