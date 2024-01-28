jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISubjectChapter, SubjectChapter } from '../subject-chapter.model';
import { SubjectChapterService } from '../service/subject-chapter.service';

import { SubjectChapterRoutingResolveService } from './subject-chapter-routing-resolve.service';

describe('Service Tests', () => {
  describe('SubjectChapter routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SubjectChapterRoutingResolveService;
    let service: SubjectChapterService;
    let resultSubjectChapter: ISubjectChapter | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SubjectChapterRoutingResolveService);
      service = TestBed.inject(SubjectChapterService);
      resultSubjectChapter = undefined;
    });

    describe('resolve', () => {
      it('should return ISubjectChapter returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSubjectChapter = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSubjectChapter).toEqual({ id: 123 });
      });

      it('should return new ISubjectChapter if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSubjectChapter = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSubjectChapter).toEqual(new SubjectChapter());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSubjectChapter = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSubjectChapter).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
