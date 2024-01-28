import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ModeOfPayment } from 'app/entities/enumerations/mode-of-payment.model';
import { IStudentPayments, StudentPayments } from '../student-payments.model';

import { StudentPaymentsService } from './student-payments.service';

describe('Service Tests', () => {
  describe('StudentPayments Service', () => {
    let service: StudentPaymentsService;
    let httpMock: HttpTestingController;
    let elemDefault: IStudentPayments;
    let expectedResult: IStudentPayments | IStudentPayments[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(StudentPaymentsService);
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

      it('should create a StudentPayments', () => {
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

        service.create(new StudentPayments()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a StudentPayments', () => {
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

      it('should partial update a StudentPayments', () => {
        const patchObject = Object.assign(
          {
            amountPaid: 1,
            upiId: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new StudentPayments()
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

      it('should return a list of StudentPayments', () => {
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

      it('should delete a StudentPayments', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addStudentPaymentsToCollectionIfMissing', () => {
        it('should add a StudentPayments to an empty array', () => {
          const studentPayments: IStudentPayments = { id: 123 };
          expectedResult = service.addStudentPaymentsToCollectionIfMissing([], studentPayments);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentPayments);
        });

        it('should not add a StudentPayments to an array that contains it', () => {
          const studentPayments: IStudentPayments = { id: 123 };
          const studentPaymentsCollection: IStudentPayments[] = [
            {
              ...studentPayments,
            },
            { id: 456 },
          ];
          expectedResult = service.addStudentPaymentsToCollectionIfMissing(studentPaymentsCollection, studentPayments);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a StudentPayments to an array that doesn't contain it", () => {
          const studentPayments: IStudentPayments = { id: 123 };
          const studentPaymentsCollection: IStudentPayments[] = [{ id: 456 }];
          expectedResult = service.addStudentPaymentsToCollectionIfMissing(studentPaymentsCollection, studentPayments);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentPayments);
        });

        it('should add only unique StudentPayments to an array', () => {
          const studentPaymentsArray: IStudentPayments[] = [{ id: 123 }, { id: 456 }, { id: 85414 }];
          const studentPaymentsCollection: IStudentPayments[] = [{ id: 123 }];
          expectedResult = service.addStudentPaymentsToCollectionIfMissing(studentPaymentsCollection, ...studentPaymentsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const studentPayments: IStudentPayments = { id: 123 };
          const studentPayments2: IStudentPayments = { id: 456 };
          expectedResult = service.addStudentPaymentsToCollectionIfMissing([], studentPayments, studentPayments2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentPayments);
          expect(expectedResult).toContain(studentPayments2);
        });

        it('should accept null and undefined values', () => {
          const studentPayments: IStudentPayments = { id: 123 };
          expectedResult = service.addStudentPaymentsToCollectionIfMissing([], null, studentPayments, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentPayments);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
