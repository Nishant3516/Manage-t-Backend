import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ClassFeeComponent } from './list/class-fee.component';
import { ClassFeeDetailComponent } from './detail/class-fee-detail.component';
import { ClassFeeUpdateComponent } from './update/class-fee-update.component';
import { ClassFeeDeleteDialogComponent } from './delete/class-fee-delete-dialog.component';
import { ClassFeeRoutingModule } from './route/class-fee-routing.module';

@NgModule({
  imports: [SharedModule, ClassFeeRoutingModule],
  declarations: [ClassFeeComponent, ClassFeeDetailComponent, ClassFeeUpdateComponent, ClassFeeDeleteDialogComponent],
  entryComponents: [ClassFeeDeleteDialogComponent],
})
export class ClassFeeModule {}
