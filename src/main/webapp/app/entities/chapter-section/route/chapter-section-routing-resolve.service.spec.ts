jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IChapterSection, ChapterSection } from '../chapter-section.model';
import { ChapterSectionService } from '../service/chapter-section.service';

import { ChapterSectionRoutingResolveService } from './chapter-section-routing-resolve.service';

describe('Service Tests', () => {
  describe('ChapterSection routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ChapterSectionRoutingResolveService;
    let service: ChapterSectionService;
    let resultChapterSection: IChapterSection | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ChapterSectionRoutingResolveService);
      service = TestBed.inject(ChapterSectionService);
      resultChapterSection = undefined;
    });

    describe('resolve', () => {
      it('should return IChapterSection returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChapterSection = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultChapterSection).toEqual({ id: 123 });
      });

      it('should return new IChapterSection if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChapterSection = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultChapterSection).toEqual(new ChapterSection());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChapterSection = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultChapterSection).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
