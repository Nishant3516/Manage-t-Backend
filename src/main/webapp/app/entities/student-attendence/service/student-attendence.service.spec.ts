import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IStudentAttendence, StudentAttendence } from '../student-attendence.model';

import { StudentAttendenceService } from './student-attendence.service';

describe('Service Tests', () => {
  describe('StudentAttendence Service', () => {
    let service: StudentAttendenceService;
    let httpMock: HttpTestingController;
    let elemDefault: IStudentAttendence;
    let expectedResult: IStudentAttendence | IStudentAttendence[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(StudentAttendenceService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        schoolDate: currentDate,
        attendence: false,
        createDate: currentDate,
        lastModified: currentDate,
        cancelDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            schoolDate: currentDate.format(DATE_FORMAT),
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

      it('should create a StudentAttendence', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            schoolDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            schoolDate: currentDate,
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.create(new StudentAttendence()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a StudentAttendence', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            schoolDate: currentDate.format(DATE_FORMAT),
            attendence: true,
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            schoolDate: currentDate,
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

      it('should partial update a StudentAttendence', () => {
        const patchObject = Object.assign(
          {
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new StudentAttendence()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            schoolDate: currentDate,
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

      it('should return a list of StudentAttendence', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            schoolDate: currentDate.format(DATE_FORMAT),
            attendence: true,
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            schoolDate: currentDate,
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

      it('should delete a StudentAttendence', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addStudentAttendenceToCollectionIfMissing', () => {
        it('should add a StudentAttendence to an empty array', () => {
          const studentAttendence: IStudentAttendence = { id: 123 };
          expectedResult = service.addStudentAttendenceToCollectionIfMissing([], studentAttendence);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentAttendence);
        });

        it('should not add a StudentAttendence to an array that contains it', () => {
          const studentAttendence: IStudentAttendence = { id: 123 };
          const studentAttendenceCollection: IStudentAttendence[] = [
            {
              ...studentAttendence,
            },
            { id: 456 },
          ];
          expectedResult = service.addStudentAttendenceToCollectionIfMissing(studentAttendenceCollection, studentAttendence);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a StudentAttendence to an array that doesn't contain it", () => {
          const studentAttendence: IStudentAttendence = { id: 123 };
          const studentAttendenceCollection: IStudentAttendence[] = [{ id: 456 }];
          expectedResult = service.addStudentAttendenceToCollectionIfMissing(studentAttendenceCollection, studentAttendence);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentAttendence);
        });

        it('should add only unique StudentAttendence to an array', () => {
          const studentAttendenceArray: IStudentAttendence[] = [{ id: 123 }, { id: 456 }, { id: 9380 }];
          const studentAttendenceCollection: IStudentAttendence[] = [{ id: 123 }];
          expectedResult = service.addStudentAttendenceToCollectionIfMissing(studentAttendenceCollection, ...studentAttendenceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const studentAttendence: IStudentAttendence = { id: 123 };
          const studentAttendence2: IStudentAttendence = { id: 456 };
          expectedResult = service.addStudentAttendenceToCollectionIfMissing([], studentAttendence, studentAttendence2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentAttendence);
          expect(expectedResult).toContain(studentAttendence2);
        });

        it('should accept null and undefined values', () => {
          const studentAttendence: IStudentAttendence = { id: 123 };
          expectedResult = service.addStudentAttendenceToCollectionIfMissing([], null, studentAttendence, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentAttendence);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
