jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISchoolNotifications, SchoolNotifications } from '../school-notifications.model';
import { SchoolNotificationsService } from '../service/school-notifications.service';

import { SchoolNotificationsRoutingResolveService } from './school-notifications-routing-resolve.service';

describe('Service Tests', () => {
  describe('SchoolNotifications routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SchoolNotificationsRoutingResolveService;
    let service: SchoolNotificationsService;
    let resultSchoolNotifications: ISchoolNotifications | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SchoolNotificationsRoutingResolveService);
      service = TestBed.inject(SchoolNotificationsService);
      resultSchoolNotifications = undefined;
    });

    describe('resolve', () => {
      it('should return ISchoolNotifications returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolNotifications = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolNotifications).toEqual({ id: 123 });
      });

      it('should return new ISchoolNotifications if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolNotifications = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSchoolNotifications).toEqual(new SchoolNotifications());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolNotifications = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolNotifications).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
