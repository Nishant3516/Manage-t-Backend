import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SchoolReportComponent } from './list/school-report.component';
import { SchoolReportDetailComponent } from './detail/school-report-detail.component';
import { SchoolReportUpdateComponent } from './update/school-report-update.component';
import { SchoolReportDeleteDialogComponent } from './delete/school-report-delete-dialog.component';
import { SchoolReportRoutingModule } from './route/school-report-routing.module';

@NgModule({
  imports: [SharedModule, SchoolReportRoutingModule],
  declarations: [SchoolReportComponent, SchoolReportDetailComponent, SchoolReportUpdateComponent, SchoolReportDeleteDialogComponent],
  entryComponents: [SchoolReportDeleteDialogComponent],
})
export class SchoolReportModule {}
