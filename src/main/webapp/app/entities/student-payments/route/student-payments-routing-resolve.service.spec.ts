jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IStudentPayments, StudentPayments } from '../student-payments.model';
import { StudentPaymentsService } from '../service/student-payments.service';

import { StudentPaymentsRoutingResolveService } from './student-payments-routing-resolve.service';

describe('Service Tests', () => {
  describe('StudentPayments routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: StudentPaymentsRoutingResolveService;
    let service: StudentPaymentsService;
    let resultStudentPayments: IStudentPayments | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(StudentPaymentsRoutingResolveService);
      service = TestBed.inject(StudentPaymentsService);
      resultStudentPayments = undefined;
    });

    describe('resolve', () => {
      it('should return IStudentPayments returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentPayments = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStudentPayments).toEqual({ id: 123 });
      });

      it('should return new IStudentPayments if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentPayments = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultStudentPayments).toEqual(new StudentPayments());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentPayments = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStudentPayments).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
