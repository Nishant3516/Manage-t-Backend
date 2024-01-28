import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchoolLedgerHead } from '../school-ledger-head.model';
import { SchoolLedgerHeadService } from '../service/school-ledger-head.service';

@Component({
  templateUrl: './school-ledger-head-delete-dialog.component.html',
})
export class SchoolLedgerHeadDeleteDialogComponent {
  schoolLedgerHead?: ISchoolLedgerHead;

  constructor(protected schoolLedgerHeadService: SchoolLedgerHeadService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.schoolLedgerHeadService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
