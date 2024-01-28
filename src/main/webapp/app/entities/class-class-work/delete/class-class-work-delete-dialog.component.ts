import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassClassWork } from '../class-class-work.model';
import { ClassClassWorkService } from '../service/class-class-work.service';

@Component({
  templateUrl: './class-class-work-delete-dialog.component.html',
})
export class ClassClassWorkDeleteDialogComponent {
  classClassWork?: IClassClassWork;

  constructor(protected classClassWorkService: ClassClassWorkService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classClassWorkService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
