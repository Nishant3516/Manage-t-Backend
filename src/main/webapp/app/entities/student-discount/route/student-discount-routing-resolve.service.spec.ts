jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IStudentDiscount, StudentDiscount } from '../student-discount.model';
import { StudentDiscountService } from '../service/student-discount.service';

import { StudentDiscountRoutingResolveService } from './student-discount-routing-resolve.service';

describe('Service Tests', () => {
  describe('StudentDiscount routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: StudentDiscountRoutingResolveService;
    let service: StudentDiscountService;
    let resultStudentDiscount: IStudentDiscount | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(StudentDiscountRoutingResolveService);
      service = TestBed.inject(StudentDiscountService);
      resultStudentDiscount = undefined;
    });

    describe('resolve', () => {
      it('should return IStudentDiscount returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentDiscount = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStudentDiscount).toEqual({ id: 123 });
      });

      it('should return new IStudentDiscount if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentDiscount = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultStudentDiscount).toEqual(new StudentDiscount());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentDiscount = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStudentDiscount).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
