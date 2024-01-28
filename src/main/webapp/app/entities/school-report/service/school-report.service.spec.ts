import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ReportType } from 'app/entities/enumerations/report-type.model';
import { ISchoolReport, SchoolReport } from '../school-report.model';

import { SchoolReportService } from './school-report.service';

describe('Service Tests', () => {
  describe('SchoolReport Service', () => {
    let service: SchoolReportService;
    let httpMock: HttpTestingController;
    let elemDefault: ISchoolReport;
    let expectedResult: ISchoolReport | ISchoolReport[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SchoolReportService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        reportType: ReportType.FEE_COLLECTION,
        startDate: currentDate,
        endDate: currentDate,
        reportFileContentType: 'image/png',
        reportFile: 'AAAAAAA',
        createDate: currentDate,
        lastModified: currentDate,
        cancelDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
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

      it('should create a SchoolReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.create(new SchoolReport()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SchoolReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            reportType: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            reportFile: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
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

      it('should partial update a SchoolReport', () => {
        const patchObject = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new SchoolReport()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
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

      it('should return a list of SchoolReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            reportType: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            reportFile: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
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

      it('should delete a SchoolReport', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSchoolReportToCollectionIfMissing', () => {
        it('should add a SchoolReport to an empty array', () => {
          const schoolReport: ISchoolReport = { id: 123 };
          expectedResult = service.addSchoolReportToCollectionIfMissing([], schoolReport);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolReport);
        });

        it('should not add a SchoolReport to an array that contains it', () => {
          const schoolReport: ISchoolReport = { id: 123 };
          const schoolReportCollection: ISchoolReport[] = [
            {
              ...schoolReport,
            },
            { id: 456 },
          ];
          expectedResult = service.addSchoolReportToCollectionIfMissing(schoolReportCollection, schoolReport);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SchoolReport to an array that doesn't contain it", () => {
          const schoolReport: ISchoolReport = { id: 123 };
          const schoolReportCollection: ISchoolReport[] = [{ id: 456 }];
          expectedResult = service.addSchoolReportToCollectionIfMissing(schoolReportCollection, schoolReport);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolReport);
        });

        it('should add only unique SchoolReport to an array', () => {
          const schoolReportArray: ISchoolReport[] = [{ id: 123 }, { id: 456 }, { id: 78402 }];
          const schoolReportCollection: ISchoolReport[] = [{ id: 123 }];
          expectedResult = service.addSchoolReportToCollectionIfMissing(schoolReportCollection, ...schoolReportArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const schoolReport: ISchoolReport = { id: 123 };
          const schoolReport2: ISchoolReport = { id: 456 };
          expectedResult = service.addSchoolReportToCollectionIfMissing([], schoolReport, schoolReport2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolReport);
          expect(expectedResult).toContain(schoolReport2);
        });

        it('should accept null and undefined values', () => {
          const schoolReport: ISchoolReport = { id: 123 };
          expectedResult = service.addSchoolReportToCollectionIfMissing([], null, schoolReport, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolReport);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
