import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuestionPaper, QuestionPaper } from '../question-paper.model';
import { QuestionPaperService } from '../service/question-paper.service';

@Injectable({ providedIn: 'root' })
export class QuestionPaperRoutingResolveService implements Resolve<IQuestionPaper> {
  constructor(protected service: QuestionPaperService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuestionPaper> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((questionPaper: HttpResponse<QuestionPaper>) => {
          if (questionPaper.body) {
            return of(questionPaper.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new QuestionPaper());
  }
}
