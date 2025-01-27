import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISchool, School } from '../school.model';

import { SchoolService } from './school.service';

describe('Service Tests', () => {
  describe('School Service', () => {
    let service: SchoolService;
    let httpMock: HttpTestingController;
    let elemDefault: ISchool;
    let expectedResult: ISchool | ISchool[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SchoolService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        groupName: 'AAAAAAA',
        schoolName: 'AAAAAAA',
        address: 'AAAAAAA',
        afflNumber: 'AAAAAAA',
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

      it('should create a School', () => {
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

        service.create(new School()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a School', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            groupName: 'BBBBBB',
            schoolName: 'BBBBBB',
            address: 'BBBBBB',
            afflNumber: 'BBBBBB',
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

      it('should partial update a School', () => {
        const patchObject = Object.assign(
          {
            groupName: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
          },
          new School()
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

      it('should return a list of School', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            groupName: 'BBBBBB',
            schoolName: 'BBBBBB',
            address: 'BBBBBB',
            afflNumber: 'BBBBBB',
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

      it('should delete a School', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSchoolToCollectionIfMissing', () => {
        it('should add a School to an empty array', () => {
          const school: ISchool = { id: 123 };
          expectedResult = service.addSchoolToCollectionIfMissing([], school);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(school);
        });

        it('should not add a School to an array that contains it', () => {
          const school: ISchool = { id: 123 };
          const schoolCollection: ISchool[] = [
            {
              ...school,
            },
            { id: 456 },
          ];
          expectedResult = service.addSchoolToCollectionIfMissing(schoolCollection, school);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a School to an array that doesn't contain it", () => {
          const school: ISchool = { id: 123 };
          const schoolCollection: ISchool[] = [{ id: 456 }];
          expectedResult = service.addSchoolToCollectionIfMissing(schoolCollection, school);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(school);
        });

        it('should add only unique School to an array', () => {
          const schoolArray: ISchool[] = [{ id: 123 }, { id: 456 }, { id: 44938 }];
          const schoolCollection: ISchool[] = [{ id: 123 }];
          expectedResult = service.addSchoolToCollectionIfMissing(schoolCollection, ...schoolArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const school: ISchool = { id: 123 };
          const school2: ISchool = { id: 456 };
          expectedResult = service.addSchoolToCollectionIfMissing([], school, school2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(school);
          expect(expectedResult).toContain(school2);
        });

        it('should accept null and undefined values', () => {
          const school: ISchool = { id: 123 };
          expectedResult = service.addSchoolToCollectionIfMissing([], null, school, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(school);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
