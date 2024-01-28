import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassLessionPlanTrack } from '../class-lession-plan-track.model';
import { ClassLessionPlanTrackService } from '../service/class-lession-plan-track.service';

@Component({
  templateUrl: './class-lession-plan-track-delete-dialog.component.html',
})
export class ClassLessionPlanTrackDeleteDialogComponent {
  classLessionPlanTrack?: IClassLessionPlanTrack;

  constructor(protected classLessionPlanTrackService: ClassLessionPlanTrackService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classLessionPlanTrackService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
