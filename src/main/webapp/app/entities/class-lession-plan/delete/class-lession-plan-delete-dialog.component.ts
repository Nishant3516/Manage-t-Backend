import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassLessionPlan } from '../class-lession-plan.model';
import { ClassLessionPlanService } from '../service/class-lession-plan.service';

@Component({
  templateUrl: './class-lession-plan-delete-dialog.component.html',
})
export class ClassLessionPlanDeleteDialogComponent {
  classLessionPlan?: IClassLessionPlan;

  constructor(protected classLessionPlanService: ClassLessionPlanService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classLessionPlanService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
