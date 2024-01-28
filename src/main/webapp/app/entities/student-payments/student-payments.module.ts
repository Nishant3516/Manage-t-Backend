import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { StudentPaymentsComponent } from './list/student-payments.component';
import { StudentPaymentsDetailComponent } from './detail/student-payments-detail.component';
import { StudentPaymentsUpdateComponent } from './update/student-payments-update.component';
import { StudentPaymentsDeleteDialogComponent } from './delete/student-payments-delete-dialog.component';
import { StudentPaymentsRoutingModule } from './route/student-payments-routing.module';

@NgModule({
  imports: [SharedModule, StudentPaymentsRoutingModule],
  declarations: [
    StudentPaymentsComponent,
    StudentPaymentsDetailComponent,
    StudentPaymentsUpdateComponent,
    StudentPaymentsDeleteDialogComponent,
  ],
  entryComponents: [StudentPaymentsDeleteDialogComponent],
})
export class StudentPaymentsModule {}
