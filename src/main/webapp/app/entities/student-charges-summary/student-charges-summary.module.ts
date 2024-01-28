import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { StudentChargesSummaryComponent } from './list/student-charges-summary.component';
import { StudentChargesSummaryDetailComponent } from './detail/student-charges-summary-detail.component';
import { StudentChargesSummaryUpdateComponent } from './update/student-charges-summary-update.component';
import { StudentChargesSummaryDeleteDialogComponent } from './delete/student-charges-summary-delete-dialog.component';
import { StudentChargesSummaryRoutingModule } from './route/student-charges-summary-routing.module';

@NgModule({
  imports: [SharedModule, StudentChargesSummaryRoutingModule],
  declarations: [
    StudentChargesSummaryComponent,
    StudentChargesSummaryDetailComponent,
    StudentChargesSummaryUpdateComponent,
    StudentChargesSummaryDeleteDialogComponent,
  ],
  entryComponents: [StudentChargesSummaryDeleteDialogComponent],
})
export class StudentChargesSummaryModule {}
