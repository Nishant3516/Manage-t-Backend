import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { StudentAttendenceComponent } from './list/student-attendence.component';
import { StudentAttendenceDetailComponent } from './detail/student-attendence-detail.component';
import { StudentAttendenceUpdateComponent } from './update/student-attendence-update.component';
import { StudentAttendenceDeleteDialogComponent } from './delete/student-attendence-delete-dialog.component';
import { StudentAttendenceRoutingModule } from './route/student-attendence-routing.module';

@NgModule({
  imports: [SharedModule, StudentAttendenceRoutingModule],
  declarations: [
    StudentAttendenceComponent,
    StudentAttendenceDetailComponent,
    StudentAttendenceUpdateComponent,
    StudentAttendenceDeleteDialogComponent,
  ],
  entryComponents: [StudentAttendenceDeleteDialogComponent],
})
export class StudentAttendenceModule {}
