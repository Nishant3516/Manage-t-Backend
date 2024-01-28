jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISchoolReport, SchoolReport } from '../school-report.model';
import { SchoolReportService } from '../service/school-report.service';

import { SchoolReportRoutingResolveService } from './school-report-routing-resolve.service';

describe('Service Tests', () => {
  describe('SchoolReport routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SchoolReportRoutingResolveService;
    let service: SchoolReportService;
    let resultSchoolReport: ISchoolReport | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SchoolReportRoutingResolveService);
      service = TestBed.inject(SchoolReportService);
      resultSchoolReport = undefined;
    });

    describe('resolve', () => {
      it('should return ISchoolReport returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolReport).toEqual({ id: 123 });
      });

      it('should return new ISchoolReport if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolReport = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSchoolReport).toEqual(new SchoolReport());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolReport).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
