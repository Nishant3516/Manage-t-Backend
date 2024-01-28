import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ClassLessionPlanTrackComponent } from './list/class-lession-plan-track.component';
import { ClassLessionPlanTrackDetailComponent } from './detail/class-lession-plan-track-detail.component';
import { ClassLessionPlanTrackUpdateComponent } from './update/class-lession-plan-track-update.component';
import { ClassLessionPlanTrackDeleteDialogComponent } from './delete/class-lession-plan-track-delete-dialog.component';
import { ClassLessionPlanTrackRoutingModule } from './route/class-lession-plan-track-routing.module';

@NgModule({
  imports: [SharedModule, ClassLessionPlanTrackRoutingModule],
  declarations: [
    ClassLessionPlanTrackComponent,
    ClassLessionPlanTrackDetailComponent,
    ClassLessionPlanTrackUpdateComponent,
    ClassLessionPlanTrackDeleteDialogComponent,
  ],
  entryComponents: [ClassLessionPlanTrackDeleteDialogComponent],
})
export class ClassLessionPlanTrackModule {}
