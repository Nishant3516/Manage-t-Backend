import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ModeOfPayment } from 'app/entities/enumerations/mode-of-payment.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { IIncomeExpenses, IncomeExpenses } from '../income-expenses.model';

import { IncomeExpensesService } from './income-expenses.service';

describe('IncomeExpenses Service', () => {
  let service: IncomeExpensesService;
  let httpMock: HttpTestingController;
  let elemDefault: IIncomeExpenses;
  let expectedResult: IIncomeExpenses | IIncomeExpenses[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IncomeExpensesService);
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

    it('should create a IncomeExpenses', () => {
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

      service.create(new IncomeExpenses()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IncomeExpenses', () => {
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

    it('should partial update a IncomeExpenses', () => {
      const patchObject = Object.assign(
        {
          amountPaid: 1,
          modeOfPay: 'BBBBBB',
          remarks: 'BBBBBB',
          lastModified: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
        },
        new IncomeExpenses()
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

    it('should return a list of IncomeExpenses', () => {
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

    it('should delete a IncomeExpenses', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIncomeExpensesToCollectionIfMissing', () => {
      it('should add a IncomeExpenses to an empty array', () => {
        const incomeExpenses: IIncomeExpenses = { id: 123 };
        expectedResult = service.addIncomeExpensesToCollectionIfMissing([], incomeExpenses);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incomeExpenses);
      });

      it('should not add a IncomeExpenses to an array that contains it', () => {
        const incomeExpenses: IIncomeExpenses = { id: 123 };
        const incomeExpensesCollection: IIncomeExpenses[] = [
          {
            ...incomeExpenses,
          },
          { id: 456 },
        ];
        expectedResult = service.addIncomeExpensesToCollectionIfMissing(incomeExpensesCollection, incomeExpenses);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IncomeExpenses to an array that doesn't contain it", () => {
        const incomeExpenses: IIncomeExpenses = { id: 123 };
        const incomeExpensesCollection: IIncomeExpenses[] = [{ id: 456 }];
        expectedResult = service.addIncomeExpensesToCollectionIfMissing(incomeExpensesCollection, incomeExpenses);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incomeExpenses);
      });

      it('should add only unique IncomeExpenses to an array', () => {
        const incomeExpensesArray: IIncomeExpenses[] = [{ id: 123 }, { id: 456 }, { id: 69926 }];
        const incomeExpensesCollection: IIncomeExpenses[] = [{ id: 123 }];
        expectedResult = service.addIncomeExpensesToCollectionIfMissing(incomeExpensesCollection, ...incomeExpensesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const incomeExpenses: IIncomeExpenses = { id: 123 };
        const incomeExpenses2: IIncomeExpenses = { id: 456 };
        expectedResult = service.addIncomeExpensesToCollectionIfMissing([], incomeExpenses, incomeExpenses2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incomeExpenses);
        expect(expectedResult).toContain(incomeExpenses2);
      });

      it('should accept null and undefined values', () => {
        const incomeExpenses: IIncomeExpenses = { id: 123 };
        expectedResult = service.addIncomeExpensesToCollectionIfMissing([], null, incomeExpenses, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incomeExpenses);
      });

      it('should return initial array if no IncomeExpenses is added', () => {
        const incomeExpensesCollection: IIncomeExpenses[] = [{ id: 123 }];
        expectedResult = service.addIncomeExpensesToCollectionIfMissing(incomeExpensesCollection, undefined, null);
        expect(expectedResult).toEqual(incomeExpensesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
