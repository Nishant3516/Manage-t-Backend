import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISTIncomeExpenses } from '../st-income-expenses.model';
import { STIncomeExpensesService } from '../service/st-income-expenses.service';

@Component({
  templateUrl: './st-income-expenses-delete-dialog.component.html',
})
export class STIncomeExpensesDeleteDialogComponent {
  sTIncomeExpenses?: ISTIncomeExpenses;

  constructor(protected sTIncomeExpensesService: STIncomeExpensesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sTIncomeExpensesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
