import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISchoolVideoGallery, SchoolVideoGallery } from '../school-video-gallery.model';

import { SchoolVideoGalleryService } from './school-video-gallery.service';

describe('Service Tests', () => {
  describe('SchoolVideoGallery Service', () => {
    let service: SchoolVideoGalleryService;
    let httpMock: HttpTestingController;
    let elemDefault: ISchoolVideoGallery;
    let expectedResult: ISchoolVideoGallery | ISchoolVideoGallery[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SchoolVideoGalleryService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        videoTitle: 'AAAAAAA',
        videoDescription: 'AAAAAAA',
        videoFileContentType: 'image/png',
        videoFile: 'AAAAAAA',
        videoLink: 'AAAAAAA',
        createDate: currentDate,
        lastModified: currentDate,
        cancelDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SchoolVideoGallery', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.create(new SchoolVideoGallery()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SchoolVideoGallery', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            videoTitle: 'BBBBBB',
            videoDescription: 'BBBBBB',
            videoFile: 'BBBBBB',
            videoLink: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SchoolVideoGallery', () => {
        const patchObject = Object.assign(
          {
            videoTitle: 'BBBBBB',
            videoDescription: 'BBBBBB',
            videoLink: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new SchoolVideoGallery()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SchoolVideoGallery', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            videoTitle: 'BBBBBB',
            videoDescription: 'BBBBBB',
            videoFile: 'BBBBBB',
            videoLink: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SchoolVideoGallery', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSchoolVideoGalleryToCollectionIfMissing', () => {
        it('should add a SchoolVideoGallery to an empty array', () => {
          const schoolVideoGallery: ISchoolVideoGallery = { id: 123 };
          expectedResult = service.addSchoolVideoGalleryToCollectionIfMissing([], schoolVideoGallery);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolVideoGallery);
        });

        it('should not add a SchoolVideoGallery to an array that contains it', () => {
          const schoolVideoGallery: ISchoolVideoGallery = { id: 123 };
          const schoolVideoGalleryCollection: ISchoolVideoGallery[] = [
            {
              ...schoolVideoGallery,
            },
            { id: 456 },
          ];
          expectedResult = service.addSchoolVideoGalleryToCollectionIfMissing(schoolVideoGalleryCollection, schoolVideoGallery);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SchoolVideoGallery to an array that doesn't contain it", () => {
          const schoolVideoGallery: ISchoolVideoGallery = { id: 123 };
          const schoolVideoGalleryCollection: ISchoolVideoGallery[] = [{ id: 456 }];
          expectedResult = service.addSchoolVideoGalleryToCollectionIfMissing(schoolVideoGalleryCollection, schoolVideoGallery);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolVideoGallery);
        });

        it('should add only unique SchoolVideoGallery to an array', () => {
          const schoolVideoGalleryArray: ISchoolVideoGallery[] = [{ id: 123 }, { id: 456 }, { id: 10395 }];
          const schoolVideoGalleryCollection: ISchoolVideoGallery[] = [{ id: 123 }];
          expectedResult = service.addSchoolVideoGalleryToCollectionIfMissing(schoolVideoGalleryCollection, ...schoolVideoGalleryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const schoolVideoGallery: ISchoolVideoGallery = { id: 123 };
          const schoolVideoGallery2: ISchoolVideoGallery = { id: 456 };
          expectedResult = service.addSchoolVideoGalleryToCollectionIfMissing([], schoolVideoGallery, schoolVideoGallery2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolVideoGallery);
          expect(expectedResult).toContain(schoolVideoGallery2);
        });

        it('should accept null and undefined values', () => {
          const schoolVideoGallery: ISchoolVideoGallery = { id: 123 };
          expectedResult = service.addSchoolVideoGalleryToCollectionIfMissing([], null, schoolVideoGallery, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolVideoGallery);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
