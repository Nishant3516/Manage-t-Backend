import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISchoolClass, SchoolClass } from '../school-class.model';

import { SchoolClassService } from './school-class.service';

describe('Service Tests', () => {
  describe('SchoolClass Service', () => {
    let service: SchoolClassService;
    let httpMock: HttpTestingController;
    let elemDefault: ISchoolClass;
    let expectedResult: ISchoolClass | ISchoolClass[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SchoolClassService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        className: 'AAAAAAA',
        classLongName: 'AAAAAAA',
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

      it('should create a SchoolClass', () => {
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

        service.create(new SchoolClass()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SchoolClass', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            className: 'BBBBBB',
            classLongName: 'BBBBBB',
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

      it('should partial update a SchoolClass', () => {
        const patchObject = Object.assign(
          {
            className: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new SchoolClass()
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

      it('should return a list of SchoolClass', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            className: 'BBBBBB',
            classLongName: 'BBBBBB',
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

      it('should delete a SchoolClass', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSchoolClassToCollectionIfMissing', () => {
        it('should add a SchoolClass to an empty array', () => {
          const schoolClass: ISchoolClass = { id: 123 };
          expectedResult = service.addSchoolClassToCollectionIfMissing([], schoolClass);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolClass);
        });

        it('should not add a SchoolClass to an array that contains it', () => {
          const schoolClass: ISchoolClass = { id: 123 };
          const schoolClassCollection: ISchoolClass[] = [
            {
              ...schoolClass,
            },
            { id: 456 },
          ];
          expectedResult = service.addSchoolClassToCollectionIfMissing(schoolClassCollection, schoolClass);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SchoolClass to an array that doesn't contain it", () => {
          const schoolClass: ISchoolClass = { id: 123 };
          const schoolClassCollection: ISchoolClass[] = [{ id: 456 }];
          expectedResult = service.addSchoolClassToCollectionIfMissing(schoolClassCollection, schoolClass);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolClass);
        });

        it('should add only unique SchoolClass to an array', () => {
          const schoolClassArray: ISchoolClass[] = [{ id: 123 }, { id: 456 }, { id: 50870 }];
          const schoolClassCollection: ISchoolClass[] = [{ id: 123 }];
          expectedResult = service.addSchoolClassToCollectionIfMissing(schoolClassCollection, ...schoolClassArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const schoolClass: ISchoolClass = { id: 123 };
          const schoolClass2: ISchoolClass = { id: 456 };
          expectedResult = service.addSchoolClassToCollectionIfMissing([], schoolClass, schoolClass2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolClass);
          expect(expectedResult).toContain(schoolClass2);
        });

        it('should accept null and undefined values', () => {
          const schoolClass: ISchoolClass = { id: 123 };
          expectedResult = service.addSchoolClassToCollectionIfMissing([], null, schoolClass, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolClass);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
