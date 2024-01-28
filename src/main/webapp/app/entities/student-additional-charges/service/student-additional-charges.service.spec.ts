import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { FeeYear } from 'app/entities/enumerations/fee-year.model';
import { IStudentAdditionalCharges, StudentAdditionalCharges } from '../student-additional-charges.model';

import { StudentAdditionalChargesService } from './student-additional-charges.service';

describe('Service Tests', () => {
  describe('StudentAdditionalCharges Service', () => {
    let service: StudentAdditionalChargesService;
    let httpMock: HttpTestingController;
    let elemDefault: IStudentAdditionalCharges;
    let expectedResult: IStudentAdditionalCharges | IStudentAdditionalCharges[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(StudentAdditionalChargesService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        feeYear: FeeYear.YEAR_2023,
        dueDate: 0,
        janAddChrg: 0,
        febAddChrgc: 0,
        marAddChrg: 0,
        aprAddChrg: 0,
        mayAddChrg: 0,
        junAddChrg: 0,
        julAddChrg: 0,
        augAddChrg: 0,
        sepAddChrg: 0,
        octAddChrg: 0,
        novAddChrg: 0,
        decAddChrg: 0,
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

      it('should create a StudentAdditionalCharges', () => {
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

        service.create(new StudentAdditionalCharges()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a StudentAdditionalCharges', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            feeYear: 'BBBBBB',
            dueDate: 1,
            janAddChrg: 1,
            febAddChrgc: 1,
            marAddChrg: 1,
            aprAddChrg: 1,
            mayAddChrg: 1,
            junAddChrg: 1,
            julAddChrg: 1,
            augAddChrg: 1,
            sepAddChrg: 1,
            octAddChrg: 1,
            novAddChrg: 1,
            decAddChrg: 1,
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

      it('should partial update a StudentAdditionalCharges', () => {
        const patchObject = Object.assign(
          {
            aprAddChrg: 1,
            mayAddChrg: 1,
            sepAddChrg: 1,
            decAddChrg: 1,
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new StudentAdditionalCharges()
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

      it('should return a list of StudentAdditionalCharges', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            feeYear: 'BBBBBB',
            dueDate: 1,
            janAddChrg: 1,
            febAddChrgc: 1,
            marAddChrg: 1,
            aprAddChrg: 1,
            mayAddChrg: 1,
            junAddChrg: 1,
            julAddChrg: 1,
            augAddChrg: 1,
            sepAddChrg: 1,
            octAddChrg: 1,
            novAddChrg: 1,
            decAddChrg: 1,
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

      it('should delete a StudentAdditionalCharges', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addStudentAdditionalChargesToCollectionIfMissing', () => {
        it('should add a StudentAdditionalCharges to an empty array', () => {
          const studentAdditionalCharges: IStudentAdditionalCharges = { id: 123 };
          expectedResult = service.addStudentAdditionalChargesToCollectionIfMissing([], studentAdditionalCharges);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentAdditionalCharges);
        });

        it('should not add a StudentAdditionalCharges to an array that contains it', () => {
          const studentAdditionalCharges: IStudentAdditionalCharges = { id: 123 };
          const studentAdditionalChargesCollection: IStudentAdditionalCharges[] = [
            {
              ...studentAdditionalCharges,
            },
            { id: 456 },
          ];
          expectedResult = service.addStudentAdditionalChargesToCollectionIfMissing(
            studentAdditionalChargesCollection,
            studentAdditionalCharges
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a StudentAdditionalCharges to an array that doesn't contain it", () => {
          const studentAdditionalCharges: IStudentAdditionalCharges = { id: 123 };
          const studentAdditionalChargesCollection: IStudentAdditionalCharges[] = [{ id: 456 }];
          expectedResult = service.addStudentAdditionalChargesToCollectionIfMissing(
            studentAdditionalChargesCollection,
            studentAdditionalCharges
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentAdditionalCharges);
        });

        it('should add only unique StudentAdditionalCharges to an array', () => {
          const studentAdditionalChargesArray: IStudentAdditionalCharges[] = [{ id: 123 }, { id: 456 }, { id: 5022 }];
          const studentAdditionalChargesCollection: IStudentAdditionalCharges[] = [{ id: 123 }];
          expectedResult = service.addStudentAdditionalChargesToCollectionIfMissing(
            studentAdditionalChargesCollection,
            ...studentAdditionalChargesArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const studentAdditionalCharges: IStudentAdditionalCharges = { id: 123 };
          const studentAdditionalCharges2: IStudentAdditionalCharges = { id: 456 };
          expectedResult = service.addStudentAdditionalChargesToCollectionIfMissing(
            [],
            studentAdditionalCharges,
            studentAdditionalCharges2
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentAdditionalCharges);
          expect(expectedResult).toContain(studentAdditionalCharges2);
        });

        it('should accept null and undefined values', () => {
          const studentAdditionalCharges: IStudentAdditionalCharges = { id: 123 };
          expectedResult = service.addStudentAdditionalChargesToCollectionIfMissing([], null, studentAdditionalCharges, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentAdditionalCharges);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
