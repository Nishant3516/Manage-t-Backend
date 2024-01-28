import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISchoolPictureGallery, SchoolPictureGallery } from '../school-picture-gallery.model';

import { SchoolPictureGalleryService } from './school-picture-gallery.service';

describe('Service Tests', () => {
  describe('SchoolPictureGallery Service', () => {
    let service: SchoolPictureGalleryService;
    let httpMock: HttpTestingController;
    let elemDefault: ISchoolPictureGallery;
    let expectedResult: ISchoolPictureGallery | ISchoolPictureGallery[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SchoolPictureGalleryService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        pictureTitle: 'AAAAAAA',
        pictureDescription: 'AAAAAAA',
        pictureFileContentType: 'image/png',
        pictureFile: 'AAAAAAA',
        pictureLink: 'AAAAAAA',
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

      it('should create a SchoolPictureGallery', () => {
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

        service.create(new SchoolPictureGallery()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SchoolPictureGallery', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pictureTitle: 'BBBBBB',
            pictureDescription: 'BBBBBB',
            pictureFile: 'BBBBBB',
            pictureLink: 'BBBBBB',
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

      it('should partial update a SchoolPictureGallery', () => {
        const patchObject = Object.assign(
          {
            pictureFile: 'BBBBBB',
            pictureLink: 'BBBBBB',
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new SchoolPictureGallery()
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

      it('should return a list of SchoolPictureGallery', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pictureTitle: 'BBBBBB',
            pictureDescription: 'BBBBBB',
            pictureFile: 'BBBBBB',
            pictureLink: 'BBBBBB',
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

      it('should delete a SchoolPictureGallery', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSchoolPictureGalleryToCollectionIfMissing', () => {
        it('should add a SchoolPictureGallery to an empty array', () => {
          const schoolPictureGallery: ISchoolPictureGallery = { id: 123 };
          expectedResult = service.addSchoolPictureGalleryToCollectionIfMissing([], schoolPictureGallery);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolPictureGallery);
        });

        it('should not add a SchoolPictureGallery to an array that contains it', () => {
          const schoolPictureGallery: ISchoolPictureGallery = { id: 123 };
          const schoolPictureGalleryCollection: ISchoolPictureGallery[] = [
            {
              ...schoolPictureGallery,
            },
            { id: 456 },
          ];
          expectedResult = service.addSchoolPictureGalleryToCollectionIfMissing(schoolPictureGalleryCollection, schoolPictureGallery);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SchoolPictureGallery to an array that doesn't contain it", () => {
          const schoolPictureGallery: ISchoolPictureGallery = { id: 123 };
          const schoolPictureGalleryCollection: ISchoolPictureGallery[] = [{ id: 456 }];
          expectedResult = service.addSchoolPictureGalleryToCollectionIfMissing(schoolPictureGalleryCollection, schoolPictureGallery);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolPictureGallery);
        });

        it('should add only unique SchoolPictureGallery to an array', () => {
          const schoolPictureGalleryArray: ISchoolPictureGallery[] = [{ id: 123 }, { id: 456 }, { id: 73753 }];
          const schoolPictureGalleryCollection: ISchoolPictureGallery[] = [{ id: 123 }];
          expectedResult = service.addSchoolPictureGalleryToCollectionIfMissing(
            schoolPictureGalleryCollection,
            ...schoolPictureGalleryArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const schoolPictureGallery: ISchoolPictureGallery = { id: 123 };
          const schoolPictureGallery2: ISchoolPictureGallery = { id: 456 };
          expectedResult = service.addSchoolPictureGalleryToCollectionIfMissing([], schoolPictureGallery, schoolPictureGallery2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolPictureGallery);
          expect(expectedResult).toContain(schoolPictureGallery2);
        });

        it('should accept null and undefined values', () => {
          const schoolPictureGallery: ISchoolPictureGallery = { id: 123 };
          expectedResult = service.addSchoolPictureGalleryToCollectionIfMissing([], null, schoolPictureGallery, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolPictureGallery);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
