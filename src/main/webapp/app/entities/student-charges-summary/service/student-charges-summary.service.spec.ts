import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IStudentChargesSummary, StudentChargesSummary } from '../student-charges-summary.model';

import { StudentChargesSummaryService } from './student-charges-summary.service';

describe('Service Tests', () => {
  describe('StudentChargesSummary Service', () => {
    let service: StudentChargesSummaryService;
    let httpMock: HttpTestingController;
    let elemDefault: IStudentChargesSummary;
    let expectedResult: IStudentChargesSummary | IStudentChargesSummary[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(StudentChargesSummaryService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        summaryType: 'AAAAAAA',
        feeYear: 'AAAAAAA',
        dueDate: 0,
        aprSummary: 'AAAAAAA',
        maySummary: 'AAAAAAA',
        junSummary: 'AAAAAAA',
        julSummary: 'AAAAAAA',
        augSummary: 'AAAAAAA',
        sepSummary: 'AAAAAAA',
        octSummary: 'AAAAAAA',
        novSummary: 'AAAAAAA',
        decSummary: 'AAAAAAA',
        janSummary: 'AAAAAAA',
        febSummary: 'AAAAAAA',
        marSummary: 'AAAAAAA',
        additionalInfo1: 'AAAAAAA',
        additionalInfo2: 'AAAAAAA',
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

      it('should create a StudentChargesSummary', () => {
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

        service.create(new StudentChargesSummary()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a StudentChargesSummary', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            summaryType: 'BBBBBB',
            feeYear: 'BBBBBB',
            dueDate: 1,
            aprSummary: 'BBBBBB',
            maySummary: 'BBBBBB',
            junSummary: 'BBBBBB',
            julSummary: 'BBBBBB',
            augSummary: 'BBBBBB',
            sepSummary: 'BBBBBB',
            octSummary: 'BBBBBB',
            novSummary: 'BBBBBB',
            decSummary: 'BBBBBB',
            janSummary: 'BBBBBB',
            febSummary: 'BBBBBB',
            marSummary: 'BBBBBB',
            additionalInfo1: 'BBBBBB',
            additionalInfo2: 'BBBBBB',
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

      it('should partial update a StudentChargesSummary', () => {
        const patchObject = Object.assign(
          {
            summaryType: 'BBBBBB',
            feeYear: 'BBBBBB',
            dueDate: 1,
            julSummary: 'BBBBBB',
            augSummary: 'BBBBBB',
            sepSummary: 'BBBBBB',
            octSummary: 'BBBBBB',
            novSummary: 'BBBBBB',
            decSummary: 'BBBBBB',
            janSummary: 'BBBBBB',
            febSummary: 'BBBBBB',
            marSummary: 'BBBBBB',
            additionalInfo2: 'BBBBBB',
          },
          new StudentChargesSummary()
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

      it('should return a list of StudentChargesSummary', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            summaryType: 'BBBBBB',
            feeYear: 'BBBBBB',
            dueDate: 1,
            aprSummary: 'BBBBBB',
            maySummary: 'BBBBBB',
            junSummary: 'BBBBBB',
            julSummary: 'BBBBBB',
            augSummary: 'BBBBBB',
            sepSummary: 'BBBBBB',
            octSummary: 'BBBBBB',
            novSummary: 'BBBBBB',
            decSummary: 'BBBBBB',
            janSummary: 'BBBBBB',
            febSummary: 'BBBBBB',
            marSummary: 'BBBBBB',
            additionalInfo1: 'BBBBBB',
            additionalInfo2: 'BBBBBB',
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

      it('should delete a StudentChargesSummary', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addStudentChargesSummaryToCollectionIfMissing', () => {
        it('should add a StudentChargesSummary to an empty array', () => {
          const studentChargesSummary: IStudentChargesSummary = { id: 123 };
          expectedResult = service.addStudentChargesSummaryToCollectionIfMissing([], studentChargesSummary);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentChargesSummary);
        });

        it('should not add a StudentChargesSummary to an array that contains it', () => {
          const studentChargesSummary: IStudentChargesSummary = { id: 123 };
          const studentChargesSummaryCollection: IStudentChargesSummary[] = [
            {
              ...studentChargesSummary,
            },
            { id: 456 },
          ];
          expectedResult = service.addStudentChargesSummaryToCollectionIfMissing(studentChargesSummaryCollection, studentChargesSummary);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a StudentChargesSummary to an array that doesn't contain it", () => {
          const studentChargesSummary: IStudentChargesSummary = { id: 123 };
          const studentChargesSummaryCollection: IStudentChargesSummary[] = [{ id: 456 }];
          expectedResult = service.addStudentChargesSummaryToCollectionIfMissing(studentChargesSummaryCollection, studentChargesSummary);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentChargesSummary);
        });

        it('should add only unique StudentChargesSummary to an array', () => {
          const studentChargesSummaryArray: IStudentChargesSummary[] = [{ id: 123 }, { id: 456 }, { id: 14459 }];
          const studentChargesSummaryCollection: IStudentChargesSummary[] = [{ id: 123 }];
          expectedResult = service.addStudentChargesSummaryToCollectionIfMissing(
            studentChargesSummaryCollection,
            ...studentChargesSummaryArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const studentChargesSummary: IStudentChargesSummary = { id: 123 };
          const studentChargesSummary2: IStudentChargesSummary = { id: 456 };
          expectedResult = service.addStudentChargesSummaryToCollectionIfMissing([], studentChargesSummary, studentChargesSummary2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentChargesSummary);
          expect(expectedResult).toContain(studentChargesSummary2);
        });

        it('should accept null and undefined values', () => {
          const studentChargesSummary: IStudentChargesSummary = { id: 123 };
          expectedResult = service.addStudentChargesSummaryToCollectionIfMissing([], null, studentChargesSummary, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentChargesSummary);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
