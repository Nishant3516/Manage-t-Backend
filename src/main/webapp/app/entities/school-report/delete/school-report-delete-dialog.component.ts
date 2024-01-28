import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchoolReport } from '../school-report.model';
import { SchoolReportService } from '../service/school-report.service';

@Component({
  templateUrl: './school-report-delete-dialog.component.html',
})
export class SchoolReportDeleteDialogComponent {
  schoolReport?: ISchoolReport;

  constructor(protected schoolReportService: SchoolReportService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.schoolReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
