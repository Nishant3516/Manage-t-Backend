import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIncomeExpenses } from '../income-expenses.model';
import { IncomeExpensesService } from '../service/income-expenses.service';

@Component({
  templateUrl: './income-expenses-delete-dialog.component.html',
})
export class IncomeExpensesDeleteDialogComponent {
  incomeExpenses?: IIncomeExpenses;

  constructor(protected incomeExpensesService: IncomeExpensesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.incomeExpensesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
