jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IClassClassWork, ClassClassWork } from '../class-class-work.model';
import { ClassClassWorkService } from '../service/class-class-work.service';

import { ClassClassWorkRoutingResolveService } from './class-class-work-routing-resolve.service';

describe('Service Tests', () => {
  describe('ClassClassWork routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ClassClassWorkRoutingResolveService;
    let service: ClassClassWorkService;
    let resultClassClassWork: IClassClassWork | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ClassClassWorkRoutingResolveService);
      service = TestBed.inject(ClassClassWorkService);
      resultClassClassWork = undefined;
    });

    describe('resolve', () => {
      it('should return IClassClassWork returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassClassWork = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassClassWork).toEqual({ id: 123 });
      });

      it('should return new IClassClassWork if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassClassWork = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultClassClassWork).toEqual(new ClassClassWork());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassClassWork = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassClassWork).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
