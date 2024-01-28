import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IdType } from 'app/entities/enumerations/id-type.model';
import { IIdStore, IdStore } from '../id-store.model';

import { IdStoreService } from './id-store.service';

describe('Service Tests', () => {
  describe('IdStore Service', () => {
    let service: IdStoreService;
    let httpMock: HttpTestingController;
    let elemDefault: IIdStore;
    let expectedResult: IIdStore | IIdStore[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(IdStoreService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        entrytype: IdType.STUDENT,
        lastGeneratedId: 0,
        startId: 0,
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

      it('should create a IdStore', () => {
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

        service.create(new IdStore()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a IdStore', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            entrytype: 'BBBBBB',
            lastGeneratedId: 1,
            startId: 1,
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

      it('should partial update a IdStore', () => {
        const patchObject = Object.assign(
          {
            createDate: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new IdStore()
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

      it('should return a list of IdStore', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            entrytype: 'BBBBBB',
            lastGeneratedId: 1,
            startId: 1,
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

      it('should delete a IdStore', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addIdStoreToCollectionIfMissing', () => {
        it('should add a IdStore to an empty array', () => {
          const idStore: IIdStore = { id: 123 };
          expectedResult = service.addIdStoreToCollectionIfMissing([], idStore);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(idStore);
        });

        it('should not add a IdStore to an array that contains it', () => {
          const idStore: IIdStore = { id: 123 };
          const idStoreCollection: IIdStore[] = [
            {
              ...idStore,
            },
            { id: 456 },
          ];
          expectedResult = service.addIdStoreToCollectionIfMissing(idStoreCollection, idStore);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a IdStore to an array that doesn't contain it", () => {
          const idStore: IIdStore = { id: 123 };
          const idStoreCollection: IIdStore[] = [{ id: 456 }];
          expectedResult = service.addIdStoreToCollectionIfMissing(idStoreCollection, idStore);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(idStore);
        });

        it('should add only unique IdStore to an array', () => {
          const idStoreArray: IIdStore[] = [{ id: 123 }, { id: 456 }, { id: 90858 }];
          const idStoreCollection: IIdStore[] = [{ id: 123 }];
          expectedResult = service.addIdStoreToCollectionIfMissing(idStoreCollection, ...idStoreArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const idStore: IIdStore = { id: 123 };
          const idStore2: IIdStore = { id: 456 };
          expectedResult = service.addIdStoreToCollectionIfMissing([], idStore, idStore2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(idStore);
          expect(expectedResult).toContain(idStore2);
        });

        it('should accept null and undefined values', () => {
          const idStore: IIdStore = { id: 123 };
          expectedResult = service.addIdStoreToCollectionIfMissing([], null, idStore, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(idStore);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
