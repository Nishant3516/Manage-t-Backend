import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchoolEvent } from '../school-event.model';
import { SchoolEventService } from '../service/school-event.service';

@Component({
  templateUrl: './school-event-delete-dialog.component.html',
})
export class SchoolEventDeleteDialogComponent {
  schoolEvent?: ISchoolEvent;

  constructor(protected schoolEventService: SchoolEventService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.schoolEventService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
