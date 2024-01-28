import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { StudentClassWorkTrackComponent } from './list/student-class-work-track.component';
import { StudentClassWorkTrackDetailComponent } from './detail/student-class-work-track-detail.component';
import { StudentClassWorkTrackUpdateComponent } from './update/student-class-work-track-update.component';
import { StudentClassWorkTrackDeleteDialogComponent } from './delete/student-class-work-track-delete-dialog.component';
import { StudentClassWorkTrackRoutingModule } from './route/student-class-work-track-routing.module';

@NgModule({
  imports: [SharedModule, StudentClassWorkTrackRoutingModule],
  declarations: [
    StudentClassWorkTrackComponent,
    StudentClassWorkTrackDetailComponent,
    StudentClassWorkTrackUpdateComponent,
    StudentClassWorkTrackDeleteDialogComponent,
  ],
  entryComponents: [StudentClassWorkTrackDeleteDialogComponent],
})
export class StudentClassWorkTrackModule {}
