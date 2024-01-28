jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IStudentAttendence, StudentAttendence } from '../student-attendence.model';
import { StudentAttendenceService } from '../service/student-attendence.service';

import { StudentAttendenceRoutingResolveService } from './student-attendence-routing-resolve.service';

describe('Service Tests', () => {
  describe('StudentAttendence routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: StudentAttendenceRoutingResolveService;
    let service: StudentAttendenceService;
    let resultStudentAttendence: IStudentAttendence | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(StudentAttendenceRoutingResolveService);
      service = TestBed.inject(StudentAttendenceService);
      resultStudentAttendence = undefined;
    });

    describe('resolve', () => {
      it('should return IStudentAttendence returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentAttendence = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStudentAttendence).toEqual({ id: 123 });
      });

      it('should return new IStudentAttendence if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentAttendence = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultStudentAttendence).toEqual(new StudentAttendence());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentAttendence = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStudentAttendence).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
