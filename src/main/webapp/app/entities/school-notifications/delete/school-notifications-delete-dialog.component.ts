import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchoolNotifications } from '../school-notifications.model';
import { SchoolNotificationsService } from '../service/school-notifications.service';

@Component({
  templateUrl: './school-notifications-delete-dialog.component.html',
})
export class SchoolNotificationsDeleteDialogComponent {
  schoolNotifications?: ISchoolNotifications;

  constructor(protected schoolNotificationsService: SchoolNotificationsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.schoolNotificationsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
