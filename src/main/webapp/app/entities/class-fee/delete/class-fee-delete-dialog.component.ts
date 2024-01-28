import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassFee } from '../class-fee.model';
import { ClassFeeService } from '../service/class-fee.service';

@Component({
  templateUrl: './class-fee-delete-dialog.component.html',
})
export class ClassFeeDeleteDialogComponent {
  classFee?: IClassFee;

  constructor(protected classFeeService: ClassFeeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classFeeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
