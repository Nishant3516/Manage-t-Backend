import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SchoolNotificationsComponent } from './list/school-notifications.component';
import { SchoolNotificationsDetailComponent } from './detail/school-notifications-detail.component';
import { SchoolNotificationsUpdateComponent } from './update/school-notifications-update.component';
import { SchoolNotificationsDeleteDialogComponent } from './delete/school-notifications-delete-dialog.component';
import { SchoolNotificationsRoutingModule } from './route/school-notifications-routing.module';

@NgModule({
  imports: [SharedModule, SchoolNotificationsRoutingModule],
  declarations: [
    SchoolNotificationsComponent,
    SchoolNotificationsDetailComponent,
    SchoolNotificationsUpdateComponent,
    SchoolNotificationsDeleteDialogComponent,
  ],
  entryComponents: [SchoolNotificationsDeleteDialogComponent],
})
export class SchoolNotificationsModule {}
