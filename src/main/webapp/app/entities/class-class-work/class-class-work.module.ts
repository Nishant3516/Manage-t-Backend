import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ClassClassWorkComponent } from './list/class-class-work.component';
import { ClassClassWorkDetailComponent } from './detail/class-class-work-detail.component';
import { ClassClassWorkUpdateComponent } from './update/class-class-work-update.component';
import { ClassClassWorkDeleteDialogComponent } from './delete/class-class-work-delete-dialog.component';
import { ClassClassWorkRoutingModule } from './route/class-class-work-routing.module';

@NgModule({
  imports: [SharedModule, ClassClassWorkRoutingModule],
  declarations: [
    ClassClassWorkComponent,
    ClassClassWorkDetailComponent,
    ClassClassWorkUpdateComponent,
    ClassClassWorkDeleteDialogComponent,
  ],
  entryComponents: [ClassClassWorkDeleteDialogComponent],
})
export class ClassClassWorkModule {}
