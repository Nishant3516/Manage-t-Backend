import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { VendorType } from 'app/entities/enumerations/vendor-type.model';
import { IVendors, Vendors } from '../vendors.model';

import { VendorsService } from './vendors.service';

describe('Vendors Service', () => {
  let service: VendorsService;
  let httpMock: HttpTestingController;
  let elemDefault: IVendors;
  let expectedResult: IVendors | IVendors[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VendorsService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      vendorPhotoContentType: 'image/png',
      vendorPhoto: 'AAAAAAA',
      vendorPhotoLink: 'AAAAAAA',
      vendorId: 'AAAAAAA',
      vendorName: 'AAAAAAA',
      phoneNumber: 'AAAAAAA',
      dateOfBirth: currentDate,
      addressLine1: 'AAAAAAA',
      addressLine2: 'AAAAAAA',
      nickName: 'AAAAAAA',
      email: 'AAAAAAA',
      createDate: currentDate,
      cancelDate: currentDate,
      vendorType: VendorType.EXTERNAL,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateOfBirth: currentDate.format(DATE_TIME_FORMAT),
          createDate: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Vendors', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateOfBirth: currentDate.format(DATE_TIME_FORMAT),
          createDate: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateOfBirth: currentDate,
          createDate: currentDate,
          cancelDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Vendors()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Vendors', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          vendorPhoto: 'BBBBBB',
          vendorPhotoLink: 'BBBBBB',
          vendorId: 'BBBBBB',
          vendorName: 'BBBBBB',
          phoneNumber: 'BBBBBB',
          dateOfBirth: currentDate.format(DATE_TIME_FORMAT),
          addressLine1: 'BBBBBB',
          addressLine2: 'BBBBBB',
          nickName: 'BBBBBB',
          email: 'BBBBBB',
          createDate: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
          vendorType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateOfBirth: currentDate,
          createDate: currentDate,
          cancelDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Vendors', () => {
      const patchObject = Object.assign(
        {
          vendorPhotoLink: 'BBBBBB',
          vendorId: 'BBBBBB',
          vendorName: 'BBBBBB',
          dateOfBirth: currentDate.format(DATE_TIME_FORMAT),
          addressLine1: 'BBBBBB',
          email: 'BBBBBB',
          createDate: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
        },
        new Vendors()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateOfBirth: currentDate,
          createDate: currentDate,
          cancelDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Vendors', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          vendorPhoto: 'BBBBBB',
          vendorPhotoLink: 'BBBBBB',
          vendorId: 'BBBBBB',
          vendorName: 'BBBBBB',
          phoneNumber: 'BBBBBB',
          dateOfBirth: currentDate.format(DATE_TIME_FORMAT),
          addressLine1: 'BBBBBB',
          addressLine2: 'BBBBBB',
          nickName: 'BBBBBB',
          email: 'BBBBBB',
          createDate: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
          vendorType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateOfBirth: currentDate,
          createDate: currentDate,
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

    it('should delete a Vendors', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVendorsToCollectionIfMissing', () => {
      it('should add a Vendors to an empty array', () => {
        const vendors: IVendors = { id: 123 };
        expectedResult = service.addVendorsToCollectionIfMissing([], vendors);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vendors);
      });

      it('should not add a Vendors to an array that contains it', () => {
        const vendors: IVendors = { id: 123 };
        const vendorsCollection: IVendors[] = [
          {
            ...vendors,
          },
          { id: 456 },
        ];
        expectedResult = service.addVendorsToCollectionIfMissing(vendorsCollection, vendors);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Vendors to an array that doesn't contain it", () => {
        const vendors: IVendors = { id: 123 };
        const vendorsCollection: IVendors[] = [{ id: 456 }];
        expectedResult = service.addVendorsToCollectionIfMissing(vendorsCollection, vendors);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vendors);
      });

      it('should add only unique Vendors to an array', () => {
        const vendorsArray: IVendors[] = [{ id: 123 }, { id: 456 }, { id: 90407 }];
        const vendorsCollection: IVendors[] = [{ id: 123 }];
        expectedResult = service.addVendorsToCollectionIfMissing(vendorsCollection, ...vendorsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vendors: IVendors = { id: 123 };
        const vendors2: IVendors = { id: 456 };
        expectedResult = service.addVendorsToCollectionIfMissing([], vendors, vendors2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vendors);
        expect(expectedResult).toContain(vendors2);
      });

      it('should accept null and undefined values', () => {
        const vendors: IVendors = { id: 123 };
        expectedResult = service.addVendorsToCollectionIfMissing([], null, vendors, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vendors);
      });

      it('should return initial array if no Vendors is added', () => {
        const vendorsCollection: IVendors[] = [{ id: 123 }];
        expectedResult = service.addVendorsToCollectionIfMissing(vendorsCollection, undefined, null);
        expect(expectedResult).toEqual(vendorsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
