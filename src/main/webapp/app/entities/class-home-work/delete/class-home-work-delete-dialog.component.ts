import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassHomeWork } from '../class-home-work.model';
import { ClassHomeWorkService } from '../service/class-home-work.service';

@Component({
  templateUrl: './class-home-work-delete-dialog.component.html',
})
export class ClassHomeWorkDeleteDialogComponent {
  classHomeWork?: IClassHomeWork;

  constructor(protected classHomeWorkService: ClassHomeWorkService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classHomeWorkService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
