import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { StudentDiscountComponent } from './list/student-discount.component';
import { StudentDiscountDetailComponent } from './detail/student-discount-detail.component';
import { StudentDiscountUpdateComponent } from './update/student-discount-update.component';
import { StudentDiscountDeleteDialogComponent } from './delete/student-discount-delete-dialog.component';
import { StudentDiscountRoutingModule } from './route/student-discount-routing.module';

@NgModule({
  imports: [SharedModule, StudentDiscountRoutingModule],
  declarations: [
    StudentDiscountComponent,
    StudentDiscountDetailComponent,
    StudentDiscountUpdateComponent,
    StudentDiscountDeleteDialogComponent,
  ],
  entryComponents: [StudentDiscountDeleteDialogComponent],
})
export class StudentDiscountModule {}
