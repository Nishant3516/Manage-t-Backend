import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISchoolVideoGallery, SchoolVideoGallery } from '../school-video-gallery.model';
import { SchoolVideoGalleryService } from '../service/school-video-gallery.service';

@Injectable({ providedIn: 'root' })
export class SchoolVideoGalleryRoutingResolveService implements Resolve<ISchoolVideoGallery> {
  constructor(protected service: SchoolVideoGalleryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISchoolVideoGallery> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((schoolVideoGallery: HttpResponse<SchoolVideoGallery>) => {
          if (schoolVideoGallery.body) {
            return of(schoolVideoGallery.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SchoolVideoGallery());
  }
}
