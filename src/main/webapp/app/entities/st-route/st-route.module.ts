import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { STRouteComponent } from './list/st-route.component';
import { STRouteDetailComponent } from './detail/st-route-detail.component';
import { STRouteUpdateComponent } from './update/st-route-update.component';
import { STRouteDeleteDialogComponent } from './delete/st-route-delete-dialog.component';
import { STRouteRoutingModule } from './route/st-route-routing.module';

@NgModule({
  imports: [SharedModule, STRouteRoutingModule],
  declarations: [STRouteComponent, STRouteDetailComponent, STRouteUpdateComponent, STRouteDeleteDialogComponent],
  entryComponents: [STRouteDeleteDialogComponent],
})
export class STRouteModule {}
