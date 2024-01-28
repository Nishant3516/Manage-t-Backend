jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IClassLessionPlan, ClassLessionPlan } from '../class-lession-plan.model';
import { ClassLessionPlanService } from '../service/class-lession-plan.service';

import { ClassLessionPlanRoutingResolveService } from './class-lession-plan-routing-resolve.service';

describe('Service Tests', () => {
  describe('ClassLessionPlan routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ClassLessionPlanRoutingResolveService;
    let service: ClassLessionPlanService;
    let resultClassLessionPlan: IClassLessionPlan | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ClassLessionPlanRoutingResolveService);
      service = TestBed.inject(ClassLessionPlanService);
      resultClassLessionPlan = undefined;
    });

    describe('resolve', () => {
      it('should return IClassLessionPlan returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassLessionPlan = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassLessionPlan).toEqual({ id: 123 });
      });

      it('should return new IClassLessionPlan if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassLessionPlan = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultClassLessionPlan).toEqual(new ClassLessionPlan());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassLessionPlan = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassLessionPlan).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
