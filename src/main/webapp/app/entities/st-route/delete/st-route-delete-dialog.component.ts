import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISTRoute } from '../st-route.model';
import { STRouteService } from '../service/st-route.service';

@Component({
  templateUrl: './st-route-delete-dialog.component.html',
})
export class STRouteDeleteDialogComponent {
  sTRoute?: ISTRoute;

  constructor(protected sTRouteService: STRouteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sTRouteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
