import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISTRoute, STRoute } from '../st-route.model';

import { STRouteService } from './st-route.service';

describe('STRoute Service', () => {
  let service: STRouteService;
  let httpMock: HttpTestingController;
  let elemDefault: ISTRoute;
  let expectedResult: ISTRoute | ISTRoute[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(STRouteService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      transportRouteName: 'AAAAAAA',
      routeCharge: 0,
      transportRouteAddress: 'AAAAAAA',
      contactNumber: 'AAAAAAA',
      createDate: currentDate,
      cancelDate: currentDate,
      remarks: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
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

    it('should create a STRoute', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createDate: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createDate: currentDate,
          cancelDate: currentDate,
        },
        returnedFromService
      );

      service.create(new STRoute()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a STRoute', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          transportRouteName: 'BBBBBB',
          routeCharge: 1,
          transportRouteAddress: 'BBBBBB',
          contactNumber: 'BBBBBB',
          createDate: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
          remarks: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
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

    it('should partial update a STRoute', () => {
      const patchObject = Object.assign(
        {
          routeCharge: 1,
          remarks: 'BBBBBB',
        },
        new STRoute()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
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

    it('should return a list of STRoute', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          transportRouteName: 'BBBBBB',
          routeCharge: 1,
          transportRouteAddress: 'BBBBBB',
          contactNumber: 'BBBBBB',
          createDate: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
          remarks: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
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

    it('should delete a STRoute', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSTRouteToCollectionIfMissing', () => {
      it('should add a STRoute to an empty array', () => {
        const sTRoute: ISTRoute = { id: 123 };
        expectedResult = service.addSTRouteToCollectionIfMissing([], sTRoute);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sTRoute);
      });

      it('should not add a STRoute to an array that contains it', () => {
        const sTRoute: ISTRoute = { id: 123 };
        const sTRouteCollection: ISTRoute[] = [
          {
            ...sTRoute,
          },
          { id: 456 },
        ];
        expectedResult = service.addSTRouteToCollectionIfMissing(sTRouteCollection, sTRoute);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a STRoute to an array that doesn't contain it", () => {
        const sTRoute: ISTRoute = { id: 123 };
        const sTRouteCollection: ISTRoute[] = [{ id: 456 }];
        expectedResult = service.addSTRouteToCollectionIfMissing(sTRouteCollection, sTRoute);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sTRoute);
      });

      it('should add only unique STRoute to an array', () => {
        const sTRouteArray: ISTRoute[] = [{ id: 123 }, { id: 456 }, { id: 84923 }];
        const sTRouteCollection: ISTRoute[] = [{ id: 123 }];
        expectedResult = service.addSTRouteToCollectionIfMissing(sTRouteCollection, ...sTRouteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sTRoute: ISTRoute = { id: 123 };
        const sTRoute2: ISTRoute = { id: 456 };
        expectedResult = service.addSTRouteToCollectionIfMissing([], sTRoute, sTRoute2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sTRoute);
        expect(expectedResult).toContain(sTRoute2);
      });

      it('should accept null and undefined values', () => {
        const sTRoute: ISTRoute = { id: 123 };
        expectedResult = service.addSTRouteToCollectionIfMissing([], null, sTRoute, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sTRoute);
      });

      it('should return initial array if no STRoute is added', () => {
        const sTRouteCollection: ISTRoute[] = [{ id: 123 }];
        expectedResult = service.addSTRouteToCollectionIfMissing(sTRouteCollection, undefined, null);
        expect(expectedResult).toEqual(sTRouteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
