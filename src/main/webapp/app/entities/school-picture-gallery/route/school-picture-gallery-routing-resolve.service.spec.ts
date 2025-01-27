jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISchoolPictureGallery, SchoolPictureGallery } from '../school-picture-gallery.model';
import { SchoolPictureGalleryService } from '../service/school-picture-gallery.service';

import { SchoolPictureGalleryRoutingResolveService } from './school-picture-gallery-routing-resolve.service';

describe('Service Tests', () => {
  describe('SchoolPictureGallery routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SchoolPictureGalleryRoutingResolveService;
    let service: SchoolPictureGalleryService;
    let resultSchoolPictureGallery: ISchoolPictureGallery | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SchoolPictureGalleryRoutingResolveService);
      service = TestBed.inject(SchoolPictureGalleryService);
      resultSchoolPictureGallery = undefined;
    });

    describe('resolve', () => {
      it('should return ISchoolPictureGallery returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolPictureGallery = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolPictureGallery).toEqual({ id: 123 });
      });

      it('should return new ISchoolPictureGallery if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolPictureGallery = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSchoolPictureGallery).toEqual(new SchoolPictureGallery());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSchoolPictureGallery = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSchoolPictureGallery).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
