jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IClassLessionPlanTrack, ClassLessionPlanTrack } from '../class-lession-plan-track.model';
import { ClassLessionPlanTrackService } from '../service/class-lession-plan-track.service';

import { ClassLessionPlanTrackRoutingResolveService } from './class-lession-plan-track-routing-resolve.service';

describe('Service Tests', () => {
  describe('ClassLessionPlanTrack routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ClassLessionPlanTrackRoutingResolveService;
    let service: ClassLessionPlanTrackService;
    let resultClassLessionPlanTrack: IClassLessionPlanTrack | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ClassLessionPlanTrackRoutingResolveService);
      service = TestBed.inject(ClassLessionPlanTrackService);
      resultClassLessionPlanTrack = undefined;
    });

    describe('resolve', () => {
      it('should return IClassLessionPlanTrack returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassLessionPlanTrack = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassLessionPlanTrack).toEqual({ id: 123 });
      });

      it('should return new IClassLessionPlanTrack if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassLessionPlanTrack = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultClassLessionPlanTrack).toEqual(new ClassLessionPlanTrack());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassLessionPlanTrack = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassLessionPlanTrack).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
