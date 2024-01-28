import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { FeeYear } from 'app/entities/enumerations/fee-year.model';
import { IClassFee, ClassFee } from '../class-fee.model';

import { ClassFeeService } from './class-fee.service';

describe('Service Tests', () => {
  describe('ClassFee Service', () => {
    let service: ClassFeeService;
    let httpMock: HttpTestingController;
    let elemDefault: IClassFee;
    let expectedResult: IClassFee | IClassFee[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ClassFeeService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        feeYear: FeeYear.YEAR_2023,
        dueDate: 0,
        janFee: 0,
        febFee: 0,
        marFee: 0,
        aprFee: 0,
        mayFee: 0,
        junFee: 0,
        julFee: 0,
        augFee: 0,
        sepFee: 0,
        octFee: 0,
        novFee: 0,
        decFee: 0,
        payByDate: currentDate,
        createDate: currentDate,
        lastModified: currentDate,
        cancelDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            payByDate: currentDate.format(DATE_FORMAT),
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

      it('should create a ClassFee', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            payByDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            payByDate: currentDate,
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.create(new ClassFee()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ClassFee', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            feeYear: 'BBBBBB',
            dueDate: 1,
            janFee: 1,
            febFee: 1,
            marFee: 1,
            aprFee: 1,
            mayFee: 1,
            junFee: 1,
            julFee: 1,
            augFee: 1,
            sepFee: 1,
            octFee: 1,
            novFee: 1,
            decFee: 1,
            payByDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            payByDate: currentDate,
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

      it('should partial update a ClassFee', () => {
        const patchObject = Object.assign(
          {
            dueDate: 1,
            marFee: 1,
            aprFee: 1,
            junFee: 1,
            julFee: 1,
            augFee: 1,
            sepFee: 1,
            novFee: 1,
            payByDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
          },
          new ClassFee()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            payByDate: currentDate,
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

      it('should return a list of ClassFee', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            feeYear: 'BBBBBB',
            dueDate: 1,
            janFee: 1,
            febFee: 1,
            marFee: 1,
            aprFee: 1,
            mayFee: 1,
            junFee: 1,
            julFee: 1,
            augFee: 1,
            sepFee: 1,
            octFee: 1,
            novFee: 1,
            decFee: 1,
            payByDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            payByDate: currentDate,
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

      it('should delete a ClassFee', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addClassFeeToCollectionIfMissing', () => {
        it('should add a ClassFee to an empty array', () => {
          const classFee: IClassFee = { id: 123 };
          expectedResult = service.addClassFeeToCollectionIfMissing([], classFee);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classFee);
        });

        it('should not add a ClassFee to an array that contains it', () => {
          const classFee: IClassFee = { id: 123 };
          const classFeeCollection: IClassFee[] = [
            {
              ...classFee,
            },
            { id: 456 },
          ];
          expectedResult = service.addClassFeeToCollectionIfMissing(classFeeCollection, classFee);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ClassFee to an array that doesn't contain it", () => {
          const classFee: IClassFee = { id: 123 };
          const classFeeCollection: IClassFee[] = [{ id: 456 }];
          expectedResult = service.addClassFeeToCollectionIfMissing(classFeeCollection, classFee);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classFee);
        });

        it('should add only unique ClassFee to an array', () => {
          const classFeeArray: IClassFee[] = [{ id: 123 }, { id: 456 }, { id: 23729 }];
          const classFeeCollection: IClassFee[] = [{ id: 123 }];
          expectedResult = service.addClassFeeToCollectionIfMissing(classFeeCollection, ...classFeeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const classFee: IClassFee = { id: 123 };
          const classFee2: IClassFee = { id: 456 };
          expectedResult = service.addClassFeeToCollectionIfMissing([], classFee, classFee2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classFee);
          expect(expectedResult).toContain(classFee2);
        });

        it('should accept null and undefined values', () => {
          const classFee: IClassFee = { id: 123 };
          expectedResult = service.addClassFeeToCollectionIfMissing([], null, classFee, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classFee);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
