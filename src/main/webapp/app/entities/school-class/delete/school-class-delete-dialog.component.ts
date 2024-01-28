import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchoolClass } from '../school-class.model';
import { SchoolClassService } from '../service/school-class.service';

@Component({
  templateUrl: './school-class-delete-dialog.component.html',
})
export class SchoolClassDeleteDialogComponent {
  schoolClass?: ISchoolClass;

  constructor(protected schoolClassService: SchoolClassService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.schoolClassService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
