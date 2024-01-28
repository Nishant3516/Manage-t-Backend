import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentHomeWorkTrack } from '../student-home-work-track.model';
import { StudentHomeWorkTrackService } from '../service/student-home-work-track.service';

@Component({
  templateUrl: './student-home-work-track-delete-dialog.component.html',
})
export class StudentHomeWorkTrackDeleteDialogComponent {
  studentHomeWorkTrack?: IStudentHomeWorkTrack;

  constructor(protected studentHomeWorkTrackService: StudentHomeWorkTrackService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentHomeWorkTrackService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
