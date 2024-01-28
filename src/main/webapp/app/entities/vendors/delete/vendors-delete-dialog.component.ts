import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVendors } from '../vendors.model';
import { VendorsService } from '../service/vendors.service';

@Component({
  templateUrl: './vendors-delete-dialog.component.html',
})
export class VendorsDeleteDialogComponent {
  vendors?: IVendors;

  constructor(protected vendorsService: VendorsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vendorsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
