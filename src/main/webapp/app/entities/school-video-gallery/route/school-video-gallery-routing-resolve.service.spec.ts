jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISchoolVideoGallery, SchoolVideoGallery } from '../school-video-gallery.model';
import { SchoolVideoGalleryService } from '../service/school-video-gallery.service';

import { SchoolVideoGalleryRoutingResolveService } from './school-video-gallery-routing-resolve.service';

describe('Service Tests', () => {
  describe('SchoolVideoGallery routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SchoolVideoGalleryRoutingResolveService;
    let service: SchoolVideoGalleryService;
    let resultSchoolVideoGallery: ISchoolVideoGallery | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SchoolVideoGalleryRoutingResolveService);
      service = TestBed.inject(SchoolVideoGalleryService);
      resultSchoolVideoGallery = undefined;
    });

    describe('resolve', () => {
      it('should return ISchoolVideoGallery returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolVideoGallery = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolVideoGallery).toEqual({ id: 123 });
      });

      it('should return new ISchoolVideoGallery if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolVideoGallery = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSchoolVideoGallery).toEqual(new SchoolVideoGallery());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolVideoGallery = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolVideoGallery).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
