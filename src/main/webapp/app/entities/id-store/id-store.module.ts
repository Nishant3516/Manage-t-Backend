import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { IdStoreComponent } from './list/id-store.component';
import { IdStoreDetailComponent } from './detail/id-store-detail.component';
import { IdStoreUpdateComponent } from './update/id-store-update.component';
import { IdStoreDeleteDialogComponent } from './delete/id-store-delete-dialog.component';
import { IdStoreRoutingModule } from './route/id-store-routing.module';

@NgModule({
  imports: [SharedModule, IdStoreRoutingModule],
  declarations: [IdStoreComponent, IdStoreDetailComponent, IdStoreUpdateComponent, IdStoreDeleteDialogComponent],
  entryComponents: [IdStoreDeleteDialogComponent],
})
export class IdStoreModule {}
