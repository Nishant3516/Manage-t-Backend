import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VendorsComponent } from './list/vendors.component';
import { VendorsDetailComponent } from './detail/vendors-detail.component';
import { VendorsUpdateComponent } from './update/vendors-update.component';
import { VendorsDeleteDialogComponent } from './delete/vendors-delete-dialog.component';
import { VendorsRoutingModule } from './route/vendors-routing.module';

@NgModule({
  imports: [SharedModule, VendorsRoutingModule],
  declarations: [VendorsComponent, VendorsDetailComponent, VendorsUpdateComponent, VendorsDeleteDialogComponent],
  entryComponents: [VendorsDeleteDialogComponent],
})
export class VendorsModule {}
