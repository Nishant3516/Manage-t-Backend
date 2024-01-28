jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISchoolLedgerHead, SchoolLedgerHead } from '../school-ledger-head.model';
import { SchoolLedgerHeadService } from '../service/school-ledger-head.service';

import { SchoolLedgerHeadRoutingResolveService } from './school-ledger-head-routing-resolve.service';

describe('Service Tests', () => {
  describe('SchoolLedgerHead routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SchoolLedgerHeadRoutingResolveService;
    let service: SchoolLedgerHeadService;
    let resultSchoolLedgerHead: ISchoolLedgerHead | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SchoolLedgerHeadRoutingResolveService);
      service = TestBed.inject(SchoolLedgerHeadService);
      resultSchoolLedgerHead = undefined;
    });

    describe('resolve', () => {
      it('should return ISchoolLedgerHead returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolLedgerHead = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolLedgerHead).toEqual({ id: 123 });
      });

      it('should return new ISchoolLedgerHead if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolLedgerHead = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSchoolLedgerHead).toEqual(new SchoolLedgerHead());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolLedgerHead = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolLedgerHead).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
