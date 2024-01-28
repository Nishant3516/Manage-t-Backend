jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IStudentAdditionalCharges, StudentAdditionalCharges } from '../student-additional-charges.model';
import { StudentAdditionalChargesService } from '../service/student-additional-charges.service';

import { StudentAdditionalChargesRoutingResolveService } from './student-additional-charges-routing-resolve.service';

describe('Service Tests', () => {
  describe('StudentAdditionalCharges routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: StudentAdditionalChargesRoutingResolveService;
    let service: StudentAdditionalChargesService;
    let resultStudentAdditionalCharges: IStudentAdditionalCharges | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(StudentAdditionalChargesRoutingResolveService);
      service = TestBed.inject(StudentAdditionalChargesService);
      resultStudentAdditionalCharges = undefined;
    });

    describe('resolve', () => {
      it('should return IStudentAdditionalCharges returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentAdditionalCharges = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStudentAdditionalCharges).toEqual({ id: 123 });
      });

      it('should return new IStudentAdditionalCharges if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentAdditionalCharges = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultStudentAdditionalCharges).toEqual(new StudentAdditionalCharges());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentAdditionalCharges = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStudentAdditionalCharges).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
