import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IQuestionPaper, QuestionPaper } from '../question-paper.model';
import { QuestionPaperService } from '../service/question-paper.service';

import { QuestionPaperRoutingResolveService } from './question-paper-routing-resolve.service';

describe('QuestionPaper routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: QuestionPaperRoutingResolveService;
  let service: QuestionPaperService;
  let resultQuestionPaper: IQuestionPaper | undefined;

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
    routingResolveService = TestBed.inject(QuestionPaperRoutingResolveService);
    service = TestBed.inject(QuestionPaperService);
    resultQuestionPaper = undefined;
  });

  describe('resolve', () => {
    it('should return IQuestionPaper returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultQuestionPaper = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultQuestionPaper).toEqual({ id: 123 });
    });

    it('should return new IQuestionPaper if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultQuestionPaper = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultQuestionPaper).toEqual(new QuestionPaper());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as QuestionPaper })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultQuestionPaper = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultQuestionPaper).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
