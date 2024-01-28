import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { StudentAssignmentType } from 'app/entities/enumerations/student-assignment-type.model';
import { IClassHomeWork, ClassHomeWork } from '../class-home-work.model';

import { ClassHomeWorkService } from './class-home-work.service';

describe('Service Tests', () => {
  describe('ClassHomeWork Service', () => {
    let service: ClassHomeWorkService;
    let httpMock: HttpTestingController;
    let elemDefault: IClassHomeWork;
    let expectedResult: IClassHomeWork | IClassHomeWork[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ClassHomeWorkService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        schoolDate: currentDate,
        studentAssignmentType: StudentAssignmentType.READING_WRITING,
        homeWorkText: 'AAAAAAA',
        homeWorkFileContentType: 'image/png',
        homeWorkFile: 'AAAAAAA',
        homeWorkFileLink: 'AAAAAAA',
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

      it('should create a ClassHomeWork', () => {
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

        service.create(new ClassHomeWork()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ClassHomeWork', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            schoolDate: currentDate.format(DATE_FORMAT),
            studentAssignmentType: 'BBBBBB',
            homeWorkText: 'BBBBBB',
            homeWorkFile: 'BBBBBB',
            homeWorkFileLink: 'BBBBBB',
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

      it('should partial update a ClassHomeWork', () => {
        const patchObject = Object.assign(
          {
            schoolDate: currentDate.format(DATE_FORMAT),
            homeWorkText: 'BBBBBB',
            homeWorkFile: 'BBBBBB',
            homeWorkFileLink: 'BBBBBB',
            assign: true,
            lastModified: currentDate.format(DATE_FORMAT),
          },
          new ClassHomeWork()
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

      it('should return a list of ClassHomeWork', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            schoolDate: currentDate.format(DATE_FORMAT),
            studentAssignmentType: 'BBBBBB',
            homeWorkText: 'BBBBBB',
            homeWorkFile: 'BBBBBB',
            homeWorkFileLink: 'BBBBBB',
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

      it('should delete a ClassHomeWork', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addClassHomeWorkToCollectionIfMissing', () => {
        it('should add a ClassHomeWork to an empty array', () => {
          const classHomeWork: IClassHomeWork = { id: 123 };
          expectedResult = service.addClassHomeWorkToCollectionIfMissing([], classHomeWork);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classHomeWork);
        });

        it('should not add a ClassHomeWork to an array that contains it', () => {
          const classHomeWork: IClassHomeWork = { id: 123 };
          const classHomeWorkCollection: IClassHomeWork[] = [
            {
              ...classHomeWork,
            },
            { id: 456 },
          ];
          expectedResult = service.addClassHomeWorkToCollectionIfMissing(classHomeWorkCollection, classHomeWork);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ClassHomeWork to an array that doesn't contain it", () => {
          const classHomeWork: IClassHomeWork = { id: 123 };
          const classHomeWorkCollection: IClassHomeWork[] = [{ id: 456 }];
          expectedResult = service.addClassHomeWorkToCollectionIfMissing(classHomeWorkCollection, classHomeWork);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classHomeWork);
        });

        it('should add only unique ClassHomeWork to an array', () => {
          const classHomeWorkArray: IClassHomeWork[] = [{ id: 123 }, { id: 456 }, { id: 67675 }];
          const classHomeWorkCollection: IClassHomeWork[] = [{ id: 123 }];
          expectedResult = service.addClassHomeWorkToCollectionIfMissing(classHomeWorkCollection, ...classHomeWorkArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const classHomeWork: IClassHomeWork = { id: 123 };
          const classHomeWork2: IClassHomeWork = { id: 456 };
          expectedResult = service.addClassHomeWorkToCollectionIfMissing([], classHomeWork, classHomeWork2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classHomeWork);
          expect(expectedResult).toContain(classHomeWork2);
        });

        it('should accept null and undefined values', () => {
          const classHomeWork: IClassHomeWork = { id: 123 };
          expectedResult = service.addClassHomeWorkToCollectionIfMissing([], null, classHomeWork, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classHomeWork);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
