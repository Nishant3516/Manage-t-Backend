import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStudentHomeWorkTrack, StudentHomeWorkTrack } from '../student-home-work-track.model';
import { StudentHomeWorkTrackService } from '../service/student-home-work-track.service';

@Injectable({ providedIn: 'root' })
export class StudentHomeWorkTrackRoutingResolveService implements Resolve<IStudentHomeWorkTrack> {
  constructor(protected service: StudentHomeWorkTrackService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudentHomeWorkTrack> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((studentHomeWorkTrack: HttpResponse<StudentHomeWorkTrack>) => {
          if (studentHomeWorkTrack.body) {
            return of(studentHomeWorkTrack.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StudentHomeWorkTrack());
  }
}
