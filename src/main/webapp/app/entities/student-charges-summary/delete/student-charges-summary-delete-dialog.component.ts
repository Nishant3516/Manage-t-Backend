import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentChargesSummary } from '../student-charges-summary.model';
import { StudentChargesSummaryService } from '../service/student-charges-summary.service';

@Component({
  templateUrl: './student-charges-summary-delete-dialog.component.html',
})
export class StudentChargesSummaryDeleteDialogComponent {
  studentChargesSummary?: IStudentChargesSummary;

  constructor(protected studentChargesSummaryService: StudentChargesSummaryService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentChargesSummaryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
