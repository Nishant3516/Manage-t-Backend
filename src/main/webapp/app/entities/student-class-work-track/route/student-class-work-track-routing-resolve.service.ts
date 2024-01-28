import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStudentClassWorkTrack, StudentClassWorkTrack } from '../student-class-work-track.model';
import { StudentClassWorkTrackService } from '../service/student-class-work-track.service';

@Injectable({ providedIn: 'root' })
export class StudentClassWorkTrackRoutingResolveService implements Resolve<IStudentClassWorkTrack> {
  constructor(protected service: StudentClassWorkTrackService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudentClassWorkTrack> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((studentClassWorkTrack: HttpResponse<StudentClassWorkTrack>) => {
          if (studentClassWorkTrack.body) {
            return of(studentClassWorkTrack.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StudentClassWorkTrack());
  }
}
