import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIdStore } from '../id-store.model';
import { IdStoreService } from '../service/id-store.service';

@Component({
  templateUrl: './id-store-delete-dialog.component.html',
})
export class IdStoreDeleteDialogComponent {
  idStore?: IIdStore;

  constructor(protected idStoreService: IdStoreService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.idStoreService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
