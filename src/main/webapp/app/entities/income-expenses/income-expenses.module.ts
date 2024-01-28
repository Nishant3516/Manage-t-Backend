import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IncomeExpensesComponent } from './list/income-expenses.component';
import { IncomeExpensesDetailComponent } from './detail/income-expenses-detail.component';
import { IncomeExpensesUpdateComponent } from './update/income-expenses-update.component';
import { IncomeExpensesDeleteDialogComponent } from './delete/income-expenses-delete-dialog.component';
import { IncomeExpensesRoutingModule } from './route/income-expenses-routing.module';

@NgModule({
  imports: [SharedModule, IncomeExpensesRoutingModule],
  declarations: [
    IncomeExpensesComponent,
    IncomeExpensesDetailComponent,
    IncomeExpensesUpdateComponent,
    IncomeExpensesDeleteDialogComponent,
  ],
  entryComponents: [IncomeExpensesDeleteDialogComponent],
})
export class IncomeExpensesModule {}
