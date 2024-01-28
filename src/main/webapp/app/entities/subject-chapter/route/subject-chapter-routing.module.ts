import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SubjectChapterComponent } from '../list/subject-chapter.component';
import { SubjectChapterDetailComponent } from '../detail/subject-chapter-detail.component';
import { SubjectChapterUpdateComponent } from '../update/subject-chapter-update.component';
import { SubjectChapterRoutingResolveService } from './subject-chapter-routing-resolve.service';

const subjectChapterRoute: Routes = [
  {
    path: '',
    component: SubjectChapterComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SubjectChapterDetailComponent,
    resolve: {
      subjectChapter: SubjectChapterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SubjectChapterUpdateComponent,
    resolve: {
      subjectChapter: SubjectChapterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SubjectChapterUpdateComponent,
    resolve: {
      subjectChapter: SubjectChapterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(subjectChapterRoute)],
  exports: [RouterModule],
})
export class SubjectChapterRoutingModule {}
