import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SchoolEventComponent } from './list/school-event.component';
import { SchoolEventDetailComponent } from './detail/school-event-detail.component';
import { SchoolEventUpdateComponent } from './update/school-event-update.component';
import { SchoolEventDeleteDialogComponent } from './delete/school-event-delete-dialog.component';
import { SchoolEventRoutingModule } from './route/school-event-routing.module';

@NgModule({
  imports: [SharedModule, SchoolEventRoutingModule],
  declarations: [SchoolEventComponent, SchoolEventDetailComponent, SchoolEventUpdateComponent, SchoolEventDeleteDialogComponent],
  entryComponents: [SchoolEventDeleteDialogComponent],
})
export class SchoolEventModule {}
