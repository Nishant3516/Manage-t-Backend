jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IClassStudent, ClassStudent } from '../class-student.model';
import { ClassStudentService } from '../service/class-student.service';

import { ClassStudentRoutingResolveService } from './class-student-routing-resolve.service';

describe('Service Tests', () => {
  describe('ClassStudent routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ClassStudentRoutingResolveService;
    let service: ClassStudentService;
    let resultClassStudent: IClassStudent | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ClassStudentRoutingResolveService);
      service = TestBed.inject(ClassStudentService);
      resultClassStudent = undefined;
    });

    describe('resolve', () => {
      it('should return IClassStudent returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassStudent = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassStudent).toEqual({ id: 123 });
      });

      it('should return new IClassStudent if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassStudent = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultClassStudent).toEqual(new ClassStudent());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassStudent = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassStudent).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
