jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIdStore, IdStore } from '../id-store.model';
import { IdStoreService } from '../service/id-store.service';

import { IdStoreRoutingResolveService } from './id-store-routing-resolve.service';

describe('Service Tests', () => {
  describe('IdStore routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: IdStoreRoutingResolveService;
    let service: IdStoreService;
    let resultIdStore: IIdStore | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(IdStoreRoutingResolveService);
      service = TestBed.inject(IdStoreService);
      resultIdStore = undefined;
    });

    describe('resolve', () => {
      it('should return IIdStore returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIdStore = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIdStore).toEqual({ id: 123 });
      });

      it('should return new IIdStore if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIdStore = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultIdStore).toEqual(new IdStore());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIdStore = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIdStore).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
