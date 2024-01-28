import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ClassSubjectComponent } from './list/class-subject.component';
import { ClassSubjectDetailComponent } from './detail/class-subject-detail.component';
import { ClassSubjectUpdateComponent } from './update/class-subject-update.component';
import { ClassSubjectDeleteDialogComponent } from './delete/class-subject-delete-dialog.component';
import { ClassSubjectRoutingModule } from './route/class-subject-routing.module';

@NgModule({
  imports: [SharedModule, ClassSubjectRoutingModule],
  declarations: [ClassSubjectComponent, ClassSubjectDetailComponent, ClassSubjectUpdateComponent, ClassSubjectDeleteDialogComponent],
  entryComponents: [ClassSubjectDeleteDialogComponent],
})
export class ClassSubjectModule {}
