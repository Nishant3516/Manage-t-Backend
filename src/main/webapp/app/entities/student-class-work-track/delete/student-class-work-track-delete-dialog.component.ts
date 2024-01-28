import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentClassWorkTrack } from '../student-class-work-track.model';
import { StudentClassWorkTrackService } from '../service/student-class-work-track.service';

@Component({
  templateUrl: './student-class-work-track-delete-dialog.component.html',
})
export class StudentClassWorkTrackDeleteDialogComponent {
  studentClassWorkTrack?: IStudentClassWorkTrack;

  constructor(protected studentClassWorkTrackService: StudentClassWorkTrackService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentClassWorkTrackService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
