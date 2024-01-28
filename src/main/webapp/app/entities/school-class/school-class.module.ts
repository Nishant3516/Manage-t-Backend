import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SchoolClassComponent } from './list/school-class.component';
import { SchoolClassDetailComponent } from './detail/school-class-detail.component';
import { SchoolClassUpdateComponent } from './update/school-class-update.component';
import { SchoolClassDeleteDialogComponent } from './delete/school-class-delete-dialog.component';
import { SchoolClassRoutingModule } from './route/school-class-routing.module';

@NgModule({
  imports: [SharedModule, SchoolClassRoutingModule],
  declarations: [SchoolClassComponent, SchoolClassDetailComponent, SchoolClassUpdateComponent, SchoolClassDeleteDialogComponent],
  entryComponents: [SchoolClassDeleteDialogComponent],
})
export class SchoolClassModule {}
