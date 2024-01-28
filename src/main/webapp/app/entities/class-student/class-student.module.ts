import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ClassStudentComponent } from './list/class-student.component';
import { ClassStudentDetailComponent } from './detail/class-student-detail.component';
import { ClassStudentUpdateComponent } from './update/class-student-update.component';
import { ClassStudentDeleteDialogComponent } from './delete/class-student-delete-dialog.component';
import { ClassStudentRoutingModule } from './route/class-student-routing.module';

@NgModule({
  imports: [SharedModule, ClassStudentRoutingModule],
  declarations: [ClassStudentComponent, ClassStudentDetailComponent, ClassStudentUpdateComponent, ClassStudentDeleteDialogComponent],
  entryComponents: [ClassStudentDeleteDialogComponent],
})
export class ClassStudentModule {}
