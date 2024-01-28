import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ClassHomeWorkComponent } from './list/class-home-work.component';
import { ClassHomeWorkDetailComponent } from './detail/class-home-work-detail.component';
import { ClassHomeWorkUpdateComponent } from './update/class-home-work-update.component';
import { ClassHomeWorkDeleteDialogComponent } from './delete/class-home-work-delete-dialog.component';
import { ClassHomeWorkRoutingModule } from './route/class-home-work-routing.module';

@NgModule({
  imports: [SharedModule, ClassHomeWorkRoutingModule],
  declarations: [ClassHomeWorkComponent, ClassHomeWorkDetailComponent, ClassHomeWorkUpdateComponent, ClassHomeWorkDeleteDialogComponent],
  entryComponents: [ClassHomeWorkDeleteDialogComponent],
})
export class ClassHomeWorkModule {}
