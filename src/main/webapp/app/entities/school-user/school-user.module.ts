import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SchoolUserComponent } from './list/school-user.component';
import { SchoolUserDetailComponent } from './detail/school-user-detail.component';
import { SchoolUserUpdateComponent } from './update/school-user-update.component';
import { SchoolUserDeleteDialogComponent } from './delete/school-user-delete-dialog.component';
import { SchoolUserRoutingModule } from './route/school-user-routing.module';

@NgModule({
  imports: [SharedModule, SchoolUserRoutingModule],
  declarations: [SchoolUserComponent, SchoolUserDetailComponent, SchoolUserUpdateComponent, SchoolUserDeleteDialogComponent],
  entryComponents: [SchoolUserDeleteDialogComponent],
})
export class SchoolUserModule {}
