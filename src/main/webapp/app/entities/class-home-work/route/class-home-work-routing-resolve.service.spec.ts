jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IClassHomeWork, ClassHomeWork } from '../class-home-work.model';
import { ClassHomeWorkService } from '../service/class-home-work.service';

import { ClassHomeWorkRoutingResolveService } from './class-home-work-routing-resolve.service';

describe('Service Tests', () => {
  describe('ClassHomeWork routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ClassHomeWorkRoutingResolveService;
    let service: ClassHomeWorkService;
    let resultClassHomeWork: IClassHomeWork | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ClassHomeWorkRoutingResolveService);
      service = TestBed.inject(ClassHomeWorkService);
      resultClassHomeWork = undefined;
    });

    describe('resolve', () => {
      it('should return IClassHomeWork returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassHomeWork = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassHomeWork).toEqual({ id: 123 });
      });

      it('should return new IClassHomeWork if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassHomeWork = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultClassHomeWork).toEqual(new ClassHomeWork());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassHomeWork = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassHomeWork).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
