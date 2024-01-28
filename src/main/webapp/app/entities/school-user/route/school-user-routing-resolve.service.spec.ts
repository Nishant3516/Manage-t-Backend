jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISchoolUser, SchoolUser } from '../school-user.model';
import { SchoolUserService } from '../service/school-user.service';

import { SchoolUserRoutingResolveService } from './school-user-routing-resolve.service';

describe('Service Tests', () => {
  describe('SchoolUser routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SchoolUserRoutingResolveService;
    let service: SchoolUserService;
    let resultSchoolUser: ISchoolUser | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SchoolUserRoutingResolveService);
      service = TestBed.inject(SchoolUserService);
      resultSchoolUser = undefined;
    });

    describe('resolve', () => {
      it('should return ISchoolUser returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolUser).toEqual({ id: 123 });
      });

      it('should return new ISchoolUser if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolUser = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSchoolUser).toEqual(new SchoolUser());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolUser).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
