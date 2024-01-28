import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuestionPaperComponent } from '../list/question-paper.component';
import { QuestionPaperDetailComponent } from '../detail/question-paper-detail.component';
import { QuestionPaperUpdateComponent } from '../update/question-paper-update.component';
import { QuestionPaperRoutingResolveService } from './question-paper-routing-resolve.service';

const questionPaperRoute: Routes = [
  {
    path: '',
    component: QuestionPaperComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuestionPaperDetailComponent,
    resolve: {
      questionPaper: QuestionPaperRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuestionPaperUpdateComponent,
    resolve: {
      questionPaper: QuestionPaperRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuestionPaperUpdateComponent,
    resolve: {
      questionPaper: QuestionPaperRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(questionPaperRoute)],
  exports: [RouterModule],
})
export class QuestionPaperRoutingModule {}
