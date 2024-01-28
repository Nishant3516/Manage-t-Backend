import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { DOWNLOADS_ROUTE } from './downloads.route';
import { DownLoadsComponent } from './downloads.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([DOWNLOADS_ROUTE])],
  declarations: [DownLoadsComponent],
})
export class DownloadsModule {}
