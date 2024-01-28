jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISchoolEvent, SchoolEvent } from '../school-event.model';
import { SchoolEventService } from '../service/school-event.service';

import { SchoolEventRoutingResolveService } from './school-event-routing-resolve.service';

describe('Service Tests', () => {
  describe('SchoolEvent routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SchoolEventRoutingResolveService;
    let service: SchoolEventService;
    let resultSchoolEvent: ISchoolEvent | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SchoolEventRoutingResolveService);
      service = TestBed.inject(SchoolEventService);
      resultSchoolEvent = undefined;
    });

    describe('resolve', () => {
      it('should return ISchoolEvent returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolEvent = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolEvent).toEqual({ id: 123 });
      });

      it('should return new ISchoolEvent if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolEvent = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSchoolEvent).toEqual(new SchoolEvent());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolEvent = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolEvent).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
