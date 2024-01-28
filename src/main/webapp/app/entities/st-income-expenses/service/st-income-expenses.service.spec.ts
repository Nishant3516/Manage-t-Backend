import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ModeOfPayment } from 'app/entities/enumerations/mode-of-payment.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { ISTIncomeExpenses, STIncomeExpenses } from '../st-income-expenses.model';

import { STIncomeExpensesService } from './st-income-expenses.service';

describe('STIncomeExpenses Service', () => {
  let service: STIncomeExpensesService;
  let httpMock: HttpTestingController;
  let elemDefault: ISTIncomeExpenses;
  let expectedResult: ISTIncomeExpenses | ISTIncomeExpenses[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(STIncomeExpensesService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      amountPaid: 0,
      modeOfPay: ModeOfPayment.CASH,
      noteNumbers: 'AAAAAAA',
      upiId: 'AAAAAAA',
      remarks: 'AAAAAAA',
      paymentDate: currentDate,
      receiptId: 'AAAAAAA',
      createDate: currentDate,
      lastModified: currentDate,
      cancelDate: currentDate,
      transactionType: TransactionType.EXPENSE,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          paymentDate: currentDate.format(DATE_FORMAT),
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

    it('should create a STIncomeExpenses', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          paymentDate: currentDate.format(DATE_FORMAT),
          createDate: currentDate.format(DATE_FORMAT),
          lastModified: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          paymentDate: currentDate,
          createDate: currentDate,
          lastModified: currentDate,
          cancelDate: currentDate,
        },
        returnedFromService
      );

      service.create(new STIncomeExpenses()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a STIncomeExpenses', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          amountPaid: 1,
          modeOfPay: 'BBBBBB',
          noteNumbers: 'BBBBBB',
          upiId: 'BBBBBB',
          remarks: 'BBBBBB',
          paymentDate: currentDate.format(DATE_FORMAT),
          receiptId: 'BBBBBB',
          createDate: currentDate.format(DATE_FORMAT),
          lastModified: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
          transactionType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          paymentDate: currentDate,
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

    it('should partial update a STIncomeExpenses', () => {
      const patchObject = Object.assign(
        {
          modeOfPay: 'BBBBBB',
          noteNumbers: 'BBBBBB',
          upiId: 'BBBBBB',
          remarks: 'BBBBBB',
          paymentDate: currentDate.format(DATE_FORMAT),
          createDate: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
        },
        new STIncomeExpenses()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          paymentDate: currentDate,
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

    it('should return a list of STIncomeExpenses', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          amountPaid: 1,
          modeOfPay: 'BBBBBB',
          noteNumbers: 'BBBBBB',
          upiId: 'BBBBBB',
          remarks: 'BBBBBB',
          paymentDate: currentDate.format(DATE_FORMAT),
          receiptId: 'BBBBBB',
          createDate: currentDate.format(DATE_FORMAT),
          lastModified: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
          transactionType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          paymentDate: currentDate,
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

    it('should delete a STIncomeExpenses', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSTIncomeExpensesToCollectionIfMissing', () => {
      it('should add a STIncomeExpenses to an empty array', () => {
        const sTIncomeExpenses: ISTIncomeExpenses = { id: 123 };
        expectedResult = service.addSTIncomeExpensesToCollectionIfMissing([], sTIncomeExpenses);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sTIncomeExpenses);
      });

      it('should not add a STIncomeExpenses to an array that contains it', () => {
        const sTIncomeExpenses: ISTIncomeExpenses = { id: 123 };
        const sTIncomeExpensesCollection: ISTIncomeExpenses[] = [
          {
            ...sTIncomeExpenses,
          },
          { id: 456 },
        ];
        expectedResult = service.addSTIncomeExpensesToCollectionIfMissing(sTIncomeExpensesCollection, sTIncomeExpenses);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a STIncomeExpenses to an array that doesn't contain it", () => {
        const sTIncomeExpenses: ISTIncomeExpenses = { id: 123 };
        const sTIncomeExpensesCollection: ISTIncomeExpenses[] = [{ id: 456 }];
        expectedResult = service.addSTIncomeExpensesToCollectionIfMissing(sTIncomeExpensesCollection, sTIncomeExpenses);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sTIncomeExpenses);
      });

      it('should add only unique STIncomeExpenses to an array', () => {
        const sTIncomeExpensesArray: ISTIncomeExpenses[] = [{ id: 123 }, { id: 456 }, { id: 68120 }];
        const sTIncomeExpensesCollection: ISTIncomeExpenses[] = [{ id: 123 }];
        expectedResult = service.addSTIncomeExpensesToCollectionIfMissing(sTIncomeExpensesCollection, ...sTIncomeExpensesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sTIncomeExpenses: ISTIncomeExpenses = { id: 123 };
        const sTIncomeExpenses2: ISTIncomeExpenses = { id: 456 };
        expectedResult = service.addSTIncomeExpensesToCollectionIfMissing([], sTIncomeExpenses, sTIncomeExpenses2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sTIncomeExpenses);
        expect(expectedResult).toContain(sTIncomeExpenses2);
      });

      it('should accept null and undefined values', () => {
        const sTIncomeExpenses: ISTIncomeExpenses = { id: 123 };
        expectedResult = service.addSTIncomeExpensesToCollectionIfMissing([], null, sTIncomeExpenses, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sTIncomeExpenses);
      });

      it('should return initial array if no STIncomeExpenses is added', () => {
        const sTIncomeExpensesCollection: ISTIncomeExpenses[] = [{ id: 123 }];
        expectedResult = service.addSTIncomeExpensesToCollectionIfMissing(sTIncomeExpensesCollection, undefined, null);
        expect(expectedResult).toEqual(sTIncomeExpensesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
