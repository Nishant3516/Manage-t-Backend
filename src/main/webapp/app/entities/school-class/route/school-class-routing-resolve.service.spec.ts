jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISchoolClass, SchoolClass } from '../school-class.model';
import { SchoolClassService } from '../service/school-class.service';

import { SchoolClassRoutingResolveService } from './school-class-routing-resolve.service';

describe('Service Tests', () => {
  describe('SchoolClass routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SchoolClassRoutingResolveService;
    let service: SchoolClassService;
    let resultSchoolClass: ISchoolClass | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SchoolClassRoutingResolveService);
      service = TestBed.inject(SchoolClassService);
      resultSchoolClass = undefined;
    });

    describe('resolve', () => {
      it('should return ISchoolClass returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolClass = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolClass).toEqual({ id: 123 });
      });

      it('should return new ISchoolClass if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolClass = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSchoolClass).toEqual(new SchoolClass());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolClass = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolClass).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
