jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IClassSubject, ClassSubject } from '../class-subject.model';
import { ClassSubjectService } from '../service/class-subject.service';

import { ClassSubjectRoutingResolveService } from './class-subject-routing-resolve.service';

describe('Service Tests', () => {
  describe('ClassSubject routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ClassSubjectRoutingResolveService;
    let service: ClassSubjectService;
    let resultClassSubject: IClassSubject | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ClassSubjectRoutingResolveService);
      service = TestBed.inject(ClassSubjectService);
      resultClassSubject = undefined;
    });

    describe('resolve', () => {
      it('should return IClassSubject returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassSubject = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassSubject).toEqual({ id: 123 });
      });

      it('should return new IClassSubject if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassSubject = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultClassSubject).toEqual(new ClassSubject());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassSubject = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassSubject).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
