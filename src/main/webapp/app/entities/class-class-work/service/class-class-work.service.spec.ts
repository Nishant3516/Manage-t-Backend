import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { StudentAssignmentType } from 'app/entities/enumerations/student-assignment-type.model';
import { IClassClassWork, ClassClassWork } from '../class-class-work.model';

import { ClassClassWorkService } from './class-class-work.service';

describe('Service Tests', () => {
  describe('ClassClassWork Service', () => {
    let service: ClassClassWorkService;
    let httpMock: HttpTestingController;
    let elemDefault: IClassClassWork;
    let expectedResult: IClassClassWork | IClassClassWork[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ClassClassWorkService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        schoolDate: currentDate,
        studentAssignmentType: StudentAssignmentType.READING_WRITING,
        classWorkText: 'AAAAAAA',
        classWorkFileContentType: 'image/png',
        classWorkFile: 'AAAAAAA',
        classWorkFileLink: 'AAAAAAA',
        assign: false,
        createDate: currentDate,
        lastModified: currentDate,
        cancelDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            schoolDate: currentDate.format(DATE_FORMAT),
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

      it('should create a ClassClassWork', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            schoolDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            schoolDate: currentDate,
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.create(new ClassClassWork()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ClassClassWork', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            schoolDate: currentDate.format(DATE_FORMAT),
            studentAssignmentType: 'BBBBBB',
            classWorkText: 'BBBBBB',
            classWorkFile: 'BBBBBB',
            classWorkFileLink: 'BBBBBB',
            assign: true,
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            schoolDate: currentDate,
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

      it('should partial update a ClassClassWork', () => {
        const patchObject = Object.assign(
          {
            studentAssignmentType: 'BBBBBB',
            classWorkText: 'BBBBBB',
            classWorkFile: 'BBBBBB',
            lastModified: currentDate.format(DATE_FORMAT),
          },
          new ClassClassWork()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            schoolDate: currentDate,
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

      it('should return a list of ClassClassWork', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            schoolDate: currentDate.format(DATE_FORMAT),
            studentAssignmentType: 'BBBBBB',
            classWorkText: 'BBBBBB',
            classWorkFile: 'BBBBBB',
            classWorkFileLink: 'BBBBBB',
            assign: true,
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            schoolDate: currentDate,
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

      it('should delete a ClassClassWork', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addClassClassWorkToCollectionIfMissing', () => {
        it('should add a ClassClassWork to an empty array', () => {
          const classClassWork: IClassClassWork = { id: 123 };
          expectedResult = service.addClassClassWorkToCollectionIfMissing([], classClassWork);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classClassWork);
        });

        it('should not add a ClassClassWork to an array that contains it', () => {
          const classClassWork: IClassClassWork = { id: 123 };
          const classClassWorkCollection: IClassClassWork[] = [
            {
              ...classClassWork,
            },
            { id: 456 },
          ];
          expectedResult = service.addClassClassWorkToCollectionIfMissing(classClassWorkCollection, classClassWork);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ClassClassWork to an array that doesn't contain it", () => {
          const classClassWork: IClassClassWork = { id: 123 };
          const classClassWorkCollection: IClassClassWork[] = [{ id: 456 }];
          expectedResult = service.addClassClassWorkToCollectionIfMissing(classClassWorkCollection, classClassWork);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classClassWork);
        });

        it('should add only unique ClassClassWork to an array', () => {
          const classClassWorkArray: IClassClassWork[] = [{ id: 123 }, { id: 456 }, { id: 53705 }];
          const classClassWorkCollection: IClassClassWork[] = [{ id: 123 }];
          expectedResult = service.addClassClassWorkToCollectionIfMissing(classClassWorkCollection, ...classClassWorkArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const classClassWork: IClassClassWork = { id: 123 };
          const classClassWork2: IClassClassWork = { id: 456 };
          expectedResult = service.addClassClassWorkToCollectionIfMissing([], classClassWork, classClassWork2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classClassWork);
          expect(expectedResult).toContain(classClassWork2);
        });

        it('should accept null and undefined values', () => {
          const classClassWork: IClassClassWork = { id: 123 };
          expectedResult = service.addClassClassWorkToCollectionIfMissing([], null, classClassWork, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classClassWork);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
