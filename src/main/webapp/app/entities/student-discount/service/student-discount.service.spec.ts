import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { FeeYear } from 'app/entities/enumerations/fee-year.model';
import { IStudentDiscount, StudentDiscount } from '../student-discount.model';

import { StudentDiscountService } from './student-discount.service';

describe('Service Tests', () => {
  describe('StudentDiscount Service', () => {
    let service: StudentDiscountService;
    let httpMock: HttpTestingController;
    let elemDefault: IStudentDiscount;
    let expectedResult: IStudentDiscount | IStudentDiscount[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(StudentDiscountService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        feeYear: FeeYear.YEAR_2023,
        dueDate: 0,
        janFeeDisc: 0,
        febFeeDisc: 0,
        marFeeDisc: 0,
        aprFeeDisc: 0,
        mayFeeDisc: 0,
        junFeeDisc: 0,
        julFeeDisc: 0,
        augFeeDisc: 0,
        sepFeeDisc: 0,
        octFeeDisc: 0,
        novFeeDisc: 0,
        decFeeDisc: 0,
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

      it('should create a StudentDiscount', () => {
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

        service.create(new StudentDiscount()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a StudentDiscount', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            feeYear: 'BBBBBB',
            dueDate: 1,
            janFeeDisc: 1,
            febFeeDisc: 1,
            marFeeDisc: 1,
            aprFeeDisc: 1,
            mayFeeDisc: 1,
            junFeeDisc: 1,
            julFeeDisc: 1,
            augFeeDisc: 1,
            sepFeeDisc: 1,
            octFeeDisc: 1,
            novFeeDisc: 1,
            decFeeDisc: 1,
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

      it('should partial update a StudentDiscount', () => {
        const patchObject = Object.assign(
          {
            feeYear: 'BBBBBB',
            dueDate: 1,
            marFeeDisc: 1,
            aprFeeDisc: 1,
            mayFeeDisc: 1,
            junFeeDisc: 1,
            julFeeDisc: 1,
            sepFeeDisc: 1,
            novFeeDisc: 1,
            createDate: currentDate.format(DATE_FORMAT),
          },
          new StudentDiscount()
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

      it('should return a list of StudentDiscount', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            feeYear: 'BBBBBB',
            dueDate: 1,
            janFeeDisc: 1,
            febFeeDisc: 1,
            marFeeDisc: 1,
            aprFeeDisc: 1,
            mayFeeDisc: 1,
            junFeeDisc: 1,
            julFeeDisc: 1,
            augFeeDisc: 1,
            sepFeeDisc: 1,
            octFeeDisc: 1,
            novFeeDisc: 1,
            decFeeDisc: 1,
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

      it('should delete a StudentDiscount', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addStudentDiscountToCollectionIfMissing', () => {
        it('should add a StudentDiscount to an empty array', () => {
          const studentDiscount: IStudentDiscount = { id: 123 };
          expectedResult = service.addStudentDiscountToCollectionIfMissing([], studentDiscount);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentDiscount);
        });

        it('should not add a StudentDiscount to an array that contains it', () => {
          const studentDiscount: IStudentDiscount = { id: 123 };
          const studentDiscountCollection: IStudentDiscount[] = [
            {
              ...studentDiscount,
            },
            { id: 456 },
          ];
          expectedResult = service.addStudentDiscountToCollectionIfMissing(studentDiscountCollection, studentDiscount);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a StudentDiscount to an array that doesn't contain it", () => {
          const studentDiscount: IStudentDiscount = { id: 123 };
          const studentDiscountCollection: IStudentDiscount[] = [{ id: 456 }];
          expectedResult = service.addStudentDiscountToCollectionIfMissing(studentDiscountCollection, studentDiscount);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentDiscount);
        });

        it('should add only unique StudentDiscount to an array', () => {
          const studentDiscountArray: IStudentDiscount[] = [{ id: 123 }, { id: 456 }, { id: 42032 }];
          const studentDiscountCollection: IStudentDiscount[] = [{ id: 123 }];
          expectedResult = service.addStudentDiscountToCollectionIfMissing(studentDiscountCollection, ...studentDiscountArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const studentDiscount: IStudentDiscount = { id: 123 };
          const studentDiscount2: IStudentDiscount = { id: 456 };
          expectedResult = service.addStudentDiscountToCollectionIfMissing([], studentDiscount, studentDiscount2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentDiscount);
          expect(expectedResult).toContain(studentDiscount2);
        });

        it('should accept null and undefined values', () => {
          const studentDiscount: IStudentDiscount = { id: 123 };
          expectedResult = service.addStudentDiscountToCollectionIfMissing([], null, studentDiscount, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentDiscount);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
