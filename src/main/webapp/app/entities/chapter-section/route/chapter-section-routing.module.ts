import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChapterSectionComponent } from '../list/chapter-section.component';
import { ChapterSectionDetailComponent } from '../detail/chapter-section-detail.component';
import { ChapterSectionUpdateComponent } from '../update/chapter-section-update.component';
import { ChapterSectionRoutingResolveService } from './chapter-section-routing-resolve.service';

const chapterSectionRoute: Routes = [
  {
    path: '',
    component: ChapterSectionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChapterSectionDetailComponent,
    resolve: {
      chapterSection: ChapterSectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChapterSectionUpdateComponent,
    resolve: {
      chapterSection: ChapterSectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChapterSectionUpdateComponent,
    resolve: {
      chapterSection: ChapterSectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chapterSectionRoute)],
  exports: [RouterModule],
})
export class ChapterSectionRoutingModule {}
