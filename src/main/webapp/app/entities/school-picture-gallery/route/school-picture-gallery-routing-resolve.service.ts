import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISchoolPictureGallery, SchoolPictureGallery } from '../school-picture-gallery.model';
import { SchoolPictureGalleryService } from '../service/school-picture-gallery.service';

@Injectable({ providedIn: 'root' })
export class SchoolPictureGalleryRoutingResolveService implements Resolve<ISchoolPictureGallery> {
  constructor(protected service: SchoolPictureGalleryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISchoolPictureGallery> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((schoolPictureGallery: HttpResponse<SchoolPictureGallery>) => {
          if (schoolPictureGallery.body) {
            return of(schoolPictureGallery.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SchoolPictureGallery());
  }
}
