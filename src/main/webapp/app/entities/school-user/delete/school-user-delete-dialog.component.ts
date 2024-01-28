import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchoolUser } from '../school-user.model';
import { SchoolUserService } from '../service/school-user.service';

@Component({
  templateUrl: './school-user-delete-dialog.component.html',
})
export class SchoolUserDeleteDialogComponent {
  schoolUser?: ISchoolUser;

  constructor(protected schoolUserService: SchoolUserService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.schoolUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
