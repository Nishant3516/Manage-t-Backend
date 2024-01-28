import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { STIncomeExpensesComponent } from './list/st-income-expenses.component';
import { STIncomeExpensesDetailComponent } from './detail/st-income-expenses-detail.component';
import { STIncomeExpensesUpdateComponent } from './update/st-income-expenses-update.component';
import { STIncomeExpensesDeleteDialogComponent } from './delete/st-income-expenses-delete-dialog.component';
import { STIncomeExpensesRoutingModule } from './route/st-income-expenses-routing.module';

@NgModule({
  imports: [SharedModule, STIncomeExpensesRoutingModule],
  declarations: [
    STIncomeExpensesComponent,
    STIncomeExpensesDetailComponent,
    STIncomeExpensesUpdateComponent,
    STIncomeExpensesDeleteDialogComponent,
  ],
  entryComponents: [STIncomeExpensesDeleteDialogComponent],
})
export class STIncomeExpensesModule {}
