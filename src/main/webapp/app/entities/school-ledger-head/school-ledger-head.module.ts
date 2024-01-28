import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SchoolLedgerHeadComponent } from './list/school-ledger-head.component';
import { SchoolLedgerHeadDetailComponent } from './detail/school-ledger-head-detail.component';
import { SchoolLedgerHeadUpdateComponent } from './update/school-ledger-head-update.component';
import { SchoolLedgerHeadDeleteDialogComponent } from './delete/school-ledger-head-delete-dialog.component';
import { SchoolLedgerHeadRoutingModule } from './route/school-ledger-head-routing.module';

@NgModule({
  imports: [SharedModule, SchoolLedgerHeadRoutingModule],
  declarations: [
    SchoolLedgerHeadComponent,
    SchoolLedgerHeadDetailComponent,
    SchoolLedgerHeadUpdateComponent,
    SchoolLedgerHeadDeleteDialogComponent,
  ],
  entryComponents: [SchoolLedgerHeadDeleteDialogComponent],
})
export class SchoolLedgerHeadModule {}
