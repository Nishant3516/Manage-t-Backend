import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAuditLog, AuditLog } from '../audit-log.model';

import { AuditLogService } from './audit-log.service';

describe('Service Tests', () => {
  describe('AuditLog Service', () => {
    let service: AuditLogService;
    let httpMock: HttpTestingController;
    let elemDefault: IAuditLog;
    let expectedResult: IAuditLog | IAuditLog[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AuditLogService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        userName: 'AAAAAAA',
        userDeviceDetails: 'AAAAAAA',
        action: 'AAAAAAA',
        data1: 'AAAAAAA',
        data2: 'AAAAAAA',
        data3: 'AAAAAAA',
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

      it('should create a AuditLog', () => {
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

        service.create(new AuditLog()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AuditLog', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            userName: 'BBBBBB',
            userDeviceDetails: 'BBBBBB',
            action: 'BBBBBB',
            data1: 'BBBBBB',
            data2: 'BBBBBB',
            data3: 'BBBBBB',
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

      it('should partial update a AuditLog', () => {
        const patchObject = Object.assign(
          {
            userName: 'BBBBBB',
            data1: 'BBBBBB',
            data2: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
          },
          new AuditLog()
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

      it('should return a list of AuditLog', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            userName: 'BBBBBB',
            userDeviceDetails: 'BBBBBB',
            action: 'BBBBBB',
            data1: 'BBBBBB',
            data2: 'BBBBBB',
            data3: 'BBBBBB',
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

      it('should delete a AuditLog', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAuditLogToCollectionIfMissing', () => {
        it('should add a AuditLog to an empty array', () => {
          const auditLog: IAuditLog = { id: 123 };
          expectedResult = service.addAuditLogToCollectionIfMissing([], auditLog);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(auditLog);
        });

        it('should not add a AuditLog to an array that contains it', () => {
          const auditLog: IAuditLog = { id: 123 };
          const auditLogCollection: IAuditLog[] = [
            {
              ...auditLog,
            },
            { id: 456 },
          ];
          expectedResult = service.addAuditLogToCollectionIfMissing(auditLogCollection, auditLog);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AuditLog to an array that doesn't contain it", () => {
          const auditLog: IAuditLog = { id: 123 };
          const auditLogCollection: IAuditLog[] = [{ id: 456 }];
          expectedResult = service.addAuditLogToCollectionIfMissing(auditLogCollection, auditLog);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(auditLog);
        });

        it('should add only unique AuditLog to an array', () => {
          const auditLogArray: IAuditLog[] = [{ id: 123 }, { id: 456 }, { id: 13572 }];
          const auditLogCollection: IAuditLog[] = [{ id: 123 }];
          expectedResult = service.addAuditLogToCollectionIfMissing(auditLogCollection, ...auditLogArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const auditLog: IAuditLog = { id: 123 };
          const auditLog2: IAuditLog = { id: 456 };
          expectedResult = service.addAuditLogToCollectionIfMissing([], auditLog, auditLog2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(auditLog);
          expect(expectedResult).toContain(auditLog2);
        });

        it('should accept null and undefined values', () => {
          const auditLog: IAuditLog = { id: 123 };
          expectedResult = service.addAuditLogToCollectionIfMissing([], null, auditLog, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(auditLog);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
