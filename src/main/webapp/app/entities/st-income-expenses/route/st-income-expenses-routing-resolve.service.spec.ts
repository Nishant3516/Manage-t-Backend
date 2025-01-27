import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ISTIncomeExpenses, STIncomeExpenses } from '../st-income-expenses.model';
import { STIncomeExpensesService } from '../service/st-income-expenses.service';

import { STIncomeExpensesRoutingResolveService } from './st-income-expenses-routing-resolve.service';

describe('STIncomeExpenses routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: STIncomeExpensesRoutingResolveService;
  let service: STIncomeExpensesService;
  let resultSTIncomeExpenses: ISTIncomeExpenses | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(STIncomeExpensesRoutingResolveService);
    service = TestBed.inject(STIncomeExpensesService);
    resultSTIncomeExpenses = undefined;
  });

  describe('resolve', () => {
    it('should return ISTIncomeExpenses returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSTIncomeExpenses = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSTIncomeExpenses).toEqual({ id: 123 });
    });

    it('should return new ISTIncomeExpenses if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSTIncomeExpenses = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSTIncomeExpenses).toEqual(new STIncomeExpenses());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as STIncomeExpenses })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSTIncomeExpenses = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSTIncomeExpenses).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
