import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { StudentHomeWorkTrackComponent } from './list/student-home-work-track.component';
import { StudentHomeWorkTrackDetailComponent } from './detail/student-home-work-track-detail.component';
import { StudentHomeWorkTrackUpdateComponent } from './update/student-home-work-track-update.component';
import { StudentHomeWorkTrackDeleteDialogComponent } from './delete/student-home-work-track-delete-dialog.component';
import { StudentHomeWorkTrackRoutingModule } from './route/student-home-work-track-routing.module';

@NgModule({
  imports: [SharedModule, StudentHomeWorkTrackRoutingModule],
  declarations: [
    StudentHomeWorkTrackComponent,
    StudentHomeWorkTrackDetailComponent,
    StudentHomeWorkTrackUpdateComponent,
    StudentHomeWorkTrackDeleteDialogComponent,
  ],
  entryComponents: [StudentHomeWorkTrackDeleteDialogComponent],
})
export class StudentHomeWorkTrackModule {}
