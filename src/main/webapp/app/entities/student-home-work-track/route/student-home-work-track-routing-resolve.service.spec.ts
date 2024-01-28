jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IStudentHomeWorkTrack, StudentHomeWorkTrack } from '../student-home-work-track.model';
import { StudentHomeWorkTrackService } from '../service/student-home-work-track.service';

import { StudentHomeWorkTrackRoutingResolveService } from './student-home-work-track-routing-resolve.service';

describe('Service Tests', () => {
  describe('StudentHomeWorkTrack routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: StudentHomeWorkTrackRoutingResolveService;
    let service: StudentHomeWorkTrackService;
    let resultStudentHomeWorkTrack: IStudentHomeWorkTrack | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(StudentHomeWorkTrackRoutingResolveService);
      service = TestBed.inject(StudentHomeWorkTrackService);
      resultStudentHomeWorkTrack = undefined;
    });

    describe('resolve', () => {
      it('should return IStudentHomeWorkTrack returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentHomeWorkTrack = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStudentHomeWorkTrack).toEqual({ id: 123 });
      });

      it('should return new IStudentHomeWorkTrack if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentHomeWorkTrack = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultStudentHomeWorkTrack).toEqual(new StudentHomeWorkTrack());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentHomeWorkTrack = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStudentHomeWorkTrack).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
