jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IClassFee, ClassFee } from '../class-fee.model';
import { ClassFeeService } from '../service/class-fee.service';

import { ClassFeeRoutingResolveService } from './class-fee-routing-resolve.service';

describe('Service Tests', () => {
  describe('ClassFee routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ClassFeeRoutingResolveService;
    let service: ClassFeeService;
    let resultClassFee: IClassFee | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ClassFeeRoutingResolveService);
      service = TestBed.inject(ClassFeeService);
      resultClassFee = undefined;
    });

    describe('resolve', () => {
      it('should return IClassFee returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassFee = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassFee).toEqual({ id: 123 });
      });

      it('should return new IClassFee if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassFee = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultClassFee).toEqual(new ClassFee());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassFee = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassFee).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
