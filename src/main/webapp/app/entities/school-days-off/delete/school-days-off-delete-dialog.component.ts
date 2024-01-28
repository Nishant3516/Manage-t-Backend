import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchoolDaysOff } from '../school-days-off.model';
import { SchoolDaysOffService } from '../service/school-days-off.service';

@Component({
  templateUrl: './school-days-off-delete-dialog.component.html',
})
export class SchoolDaysOffDeleteDialogComponent {
  schoolDaysOff?: ISchoolDaysOff;

  constructor(protected schoolDaysOffService: SchoolDaysOffService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.schoolDaysOffService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
