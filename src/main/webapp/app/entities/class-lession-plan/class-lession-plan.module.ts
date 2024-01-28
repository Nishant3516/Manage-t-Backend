import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ClassLessionPlanComponent } from './list/class-lession-plan.component';
import { ClassLessionPlanDetailComponent } from './detail/class-lession-plan-detail.component';
import { ClassLessionPlanUpdateComponent } from './update/class-lession-plan-update.component';
import { ClassLessionPlanDeleteDialogComponent } from './delete/class-lession-plan-delete-dialog.component';
import { ClassLessionPlanRoutingModule } from './route/class-lession-plan-routing.module';

@NgModule({
  imports: [SharedModule, ClassLessionPlanRoutingModule],
  declarations: [
    ClassLessionPlanComponent,
    ClassLessionPlanDetailComponent,
    ClassLessionPlanUpdateComponent,
    ClassLessionPlanDeleteDialogComponent,
  ],
  entryComponents: [ClassLessionPlanDeleteDialogComponent],
})
export class ClassLessionPlanModule {}
