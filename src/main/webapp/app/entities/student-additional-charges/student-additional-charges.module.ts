import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { StudentAdditionalChargesComponent } from './list/student-additional-charges.component';
import { StudentAdditionalChargesDetailComponent } from './detail/student-additional-charges-detail.component';
import { StudentAdditionalChargesUpdateComponent } from './update/student-additional-charges-update.component';
import { StudentAdditionalChargesDeleteDialogComponent } from './delete/student-additional-charges-delete-dialog.component';
import { StudentAdditionalChargesRoutingModule } from './route/student-additional-charges-routing.module';

@NgModule({
  imports: [SharedModule, StudentAdditionalChargesRoutingModule],
  declarations: [
    StudentAdditionalChargesComponent,
    StudentAdditionalChargesDetailComponent,
    StudentAdditionalChargesUpdateComponent,
    StudentAdditionalChargesDeleteDialogComponent,
  ],
  entryComponents: [StudentAdditionalChargesDeleteDialogComponent],
})
export class StudentAdditionalChargesModule {}
