import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChapterSection, ChapterSection } from '../chapter-section.model';
import { ChapterSectionService } from '../service/chapter-section.service';

@Injectable({ providedIn: 'root' })
export class ChapterSectionRoutingResolveService implements Resolve<IChapterSection> {
  constructor(protected service: ChapterSectionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChapterSection> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((chapterSection: HttpResponse<ChapterSection>) => {
          if (chapterSection.body) {
            return of(chapterSection.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ChapterSection());
  }
}
