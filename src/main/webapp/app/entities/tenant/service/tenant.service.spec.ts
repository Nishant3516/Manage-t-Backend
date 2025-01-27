import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITenant, Tenant } from '../tenant.model';

import { TenantService } from './tenant.service';

describe('Tenant Service', () => {
  let service: TenantService;
  let httpMock: HttpTestingController;
  let elemDefault: ITenant;
  let expectedResult: ITenant | ITenant[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TenantService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      tenantName: 'AAAAAAA',
      tenantLogoContentType: 'image/png',
      tenantLogo: 'AAAAAAA',
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

    it('should create a Tenant', () => {
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

      service.create(new Tenant()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tenant', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tenantName: 'BBBBBB',
          tenantLogo: 'BBBBBB',
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

    it('should partial update a Tenant', () => {
      const patchObject = Object.assign(
        {
          tenantName: 'BBBBBB',
          createDate: currentDate.format(DATE_FORMAT),
        },
        new Tenant()
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

    it('should return a list of Tenant', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tenantName: 'BBBBBB',
          tenantLogo: 'BBBBBB',
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

    it('should delete a Tenant', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTenantToCollectionIfMissing', () => {
      it('should add a Tenant to an empty array', () => {
        const tenant: ITenant = { id: 123 };
        expectedResult = service.addTenantToCollectionIfMissing([], tenant);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tenant);
      });

      it('should not add a Tenant to an array that contains it', () => {
        const tenant: ITenant = { id: 123 };
        const tenantCollection: ITenant[] = [
          {
            ...tenant,
          },
          { id: 456 },
        ];
        expectedResult = service.addTenantToCollectionIfMissing(tenantCollection, tenant);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tenant to an array that doesn't contain it", () => {
        const tenant: ITenant = { id: 123 };
        const tenantCollection: ITenant[] = [{ id: 456 }];
        expectedResult = service.addTenantToCollectionIfMissing(tenantCollection, tenant);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tenant);
      });

      it('should add only unique Tenant to an array', () => {
        const tenantArray: ITenant[] = [{ id: 123 }, { id: 456 }, { id: 39647 }];
        const tenantCollection: ITenant[] = [{ id: 123 }];
        expectedResult = service.addTenantToCollectionIfMissing(tenantCollection, ...tenantArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tenant: ITenant = { id: 123 };
        const tenant2: ITenant = { id: 456 };
        expectedResult = service.addTenantToCollectionIfMissing([], tenant, tenant2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tenant);
        expect(expectedResult).toContain(tenant2);
      });

      it('should accept null and undefined values', () => {
        const tenant: ITenant = { id: 123 };
        expectedResult = service.addTenantToCollectionIfMissing([], null, tenant, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tenant);
      });

      it('should return initial array if no Tenant is added', () => {
        const tenantCollection: ITenant[] = [{ id: 123 }];
        expectedResult = service.addTenantToCollectionIfMissing(tenantCollection, undefined, null);
        expect(expectedResult).toEqual(tenantCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
