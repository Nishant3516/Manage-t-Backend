import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { SchoolLedgerHeadType } from 'app/entities/enumerations/school-ledger-head-type.model';
import { ISchoolLedgerHead, SchoolLedgerHead } from '../school-ledger-head.model';

import { SchoolLedgerHeadService } from './school-ledger-head.service';

describe('Service Tests', () => {
  describe('SchoolLedgerHead Service', () => {
    let service: SchoolLedgerHeadService;
    let httpMock: HttpTestingController;
    let elemDefault: ISchoolLedgerHead;
    let expectedResult: ISchoolLedgerHead | ISchoolLedgerHead[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SchoolLedgerHeadService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        studentLedgerHeadType: SchoolLedgerHeadType.FEE,
        ledgerHeadName: 'AAAAAAA',
        ledgerHeadLongName: 'AAAAAAA',
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

      it('should create a SchoolLedgerHead', () => {
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

        service.create(new SchoolLedgerHead()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SchoolLedgerHead', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            studentLedgerHeadType: 'BBBBBB',
            ledgerHeadName: 'BBBBBB',
            ledgerHeadLongName: 'BBBBBB',
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

      it('should partial update a SchoolLedgerHead', () => {
        const patchObject = Object.assign(
          {
            studentLedgerHeadType: 'BBBBBB',
            ledgerHeadName: 'BBBBBB',
            ledgerHeadLongName: 'BBBBBB',
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new SchoolLedgerHead()
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

      it('should return a list of SchoolLedgerHead', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            studentLedgerHeadType: 'BBBBBB',
            ledgerHeadName: 'BBBBBB',
            ledgerHeadLongName: 'BBBBBB',
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

      it('should delete a SchoolLedgerHead', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSchoolLedgerHeadToCollectionIfMissing', () => {
        it('should add a SchoolLedgerHead to an empty array', () => {
          const schoolLedgerHead: ISchoolLedgerHead = { id: 123 };
          expectedResult = service.addSchoolLedgerHeadToCollectionIfMissing([], schoolLedgerHead);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolLedgerHead);
        });

        it('should not add a SchoolLedgerHead to an array that contains it', () => {
          const schoolLedgerHead: ISchoolLedgerHead = { id: 123 };
          const schoolLedgerHeadCollection: ISchoolLedgerHead[] = [
            {
              ...schoolLedgerHead,
            },
            { id: 456 },
          ];
          expectedResult = service.addSchoolLedgerHeadToCollectionIfMissing(schoolLedgerHeadCollection, schoolLedgerHead);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SchoolLedgerHead to an array that doesn't contain it", () => {
          const schoolLedgerHead: ISchoolLedgerHead = { id: 123 };
          const schoolLedgerHeadCollection: ISchoolLedgerHead[] = [{ id: 456 }];
          expectedResult = service.addSchoolLedgerHeadToCollectionIfMissing(schoolLedgerHeadCollection, schoolLedgerHead);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolLedgerHead);
        });

        it('should add only unique SchoolLedgerHead to an array', () => {
          const schoolLedgerHeadArray: ISchoolLedgerHead[] = [{ id: 123 }, { id: 456 }, { id: 56080 }];
          const schoolLedgerHeadCollection: ISchoolLedgerHead[] = [{ id: 123 }];
          expectedResult = service.addSchoolLedgerHeadToCollectionIfMissing(schoolLedgerHeadCollection, ...schoolLedgerHeadArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const schoolLedgerHead: ISchoolLedgerHead = { id: 123 };
          const schoolLedgerHead2: ISchoolLedgerHead = { id: 456 };
          expectedResult = service.addSchoolLedgerHeadToCollectionIfMissing([], schoolLedgerHead, schoolLedgerHead2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolLedgerHead);
          expect(expectedResult).toContain(schoolLedgerHead2);
        });

        it('should accept null and undefined values', () => {
          const schoolLedgerHead: ISchoolLedgerHead = { id: 123 };
          expectedResult = service.addSchoolLedgerHeadToCollectionIfMissing([], null, schoolLedgerHead, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolLedgerHead);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
