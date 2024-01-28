import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SchoolDaysOffComponent } from './list/school-days-off.component';
import { SchoolDaysOffDetailComponent } from './detail/school-days-off-detail.component';
import { SchoolDaysOffUpdateComponent } from './update/school-days-off-update.component';
import { SchoolDaysOffDeleteDialogComponent } from './delete/school-days-off-delete-dialog.component';
import { SchoolDaysOffRoutingModule } from './route/school-days-off-routing.module';

@NgModule({
  imports: [SharedModule, SchoolDaysOffRoutingModule],
  declarations: [SchoolDaysOffComponent, SchoolDaysOffDetailComponent, SchoolDaysOffUpdateComponent, SchoolDaysOffDeleteDialogComponent],
  entryComponents: [SchoolDaysOffDeleteDialogComponent],
})
export class SchoolDaysOffModule {}
