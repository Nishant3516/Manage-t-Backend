import { Route } from '@angular/router';

import { DownLoadsComponent } from './downloads.component';

export const DOWNLOADS_ROUTE: Route = {
  path: '',
  component: DownLoadsComponent,
  data: {
    pageTitle: 'downloads.title',
  },
};
