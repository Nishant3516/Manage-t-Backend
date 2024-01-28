import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubjectChapter, SubjectChapter } from '../subject-chapter.model';
import { SubjectChapterService } from '../service/subject-chapter.service';

@Injectable({ providedIn: 'root' })
export class SubjectChapterRoutingResolveService implements Resolve<ISubjectChapter> {
  constructor(protected service: SubjectChapterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISubjectChapter> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((subjectChapter: HttpResponse<SubjectChapter>) => {
          if (subjectChapter.body) {
            return of(subjectChapter.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SubjectChapter());
  }
}
