jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISchoolDaysOff, SchoolDaysOff } from '../school-days-off.model';
import { SchoolDaysOffService } from '../service/school-days-off.service';

import { SchoolDaysOffRoutingResolveService } from './school-days-off-routing-resolve.service';

describe('Service Tests', () => {
  describe('SchoolDaysOff routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SchoolDaysOffRoutingResolveService;
    let service: SchoolDaysOffService;
    let resultSchoolDaysOff: ISchoolDaysOff | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SchoolDaysOffRoutingResolveService);
      service = TestBed.inject(SchoolDaysOffService);
      resultSchoolDaysOff = undefined;
    });

    describe('resolve', () => {
      it('should return ISchoolDaysOff returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolDaysOff = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolDaysOff).toEqual({ id: 123 });
      });

      it('should return new ISchoolDaysOff if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolDaysOff = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSchoolDaysOff).toEqual(new SchoolDaysOff());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolDaysOff = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolDaysOff).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
