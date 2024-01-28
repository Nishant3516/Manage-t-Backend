import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISubjectChapter, SubjectChapter } from '../subject-chapter.model';

import { SubjectChapterService } from './subject-chapter.service';

describe('Service Tests', () => {
  describe('SubjectChapter Service', () => {
    let service: SubjectChapterService;
    let httpMock: HttpTestingController;
    let elemDefault: ISubjectChapter;
    let expectedResult: ISubjectChapter | ISubjectChapter[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SubjectChapterService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        chapterName: 'AAAAAAA',
        chapterNumber: 0,
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

      it('should create a SubjectChapter', () => {
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

        service.create(new SubjectChapter()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SubjectChapter', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            chapterName: 'BBBBBB',
            chapterNumber: 1,
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

      it('should partial update a SubjectChapter', () => {
        const patchObject = Object.assign(
          {
            chapterNumber: 1,
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new SubjectChapter()
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

      it('should return a list of SubjectChapter', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            chapterName: 'BBBBBB',
            chapterNumber: 1,
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

      it('should delete a SubjectChapter', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSubjectChapterToCollectionIfMissing', () => {
        it('should add a SubjectChapter to an empty array', () => {
          const subjectChapter: ISubjectChapter = { id: 123 };
          expectedResult = service.addSubjectChapterToCollectionIfMissing([], subjectChapter);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(subjectChapter);
        });

        it('should not add a SubjectChapter to an array that contains it', () => {
          const subjectChapter: ISubjectChapter = { id: 123 };
          const subjectChapterCollection: ISubjectChapter[] = [
            {
              ...subjectChapter,
            },
            { id: 456 },
          ];
          expectedResult = service.addSubjectChapterToCollectionIfMissing(subjectChapterCollection, subjectChapter);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SubjectChapter to an array that doesn't contain it", () => {
          const subjectChapter: ISubjectChapter = { id: 123 };
          const subjectChapterCollection: ISubjectChapter[] = [{ id: 456 }];
          expectedResult = service.addSubjectChapterToCollectionIfMissing(subjectChapterCollection, subjectChapter);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(subjectChapter);
        });

        it('should add only unique SubjectChapter to an array', () => {
          const subjectChapterArray: ISubjectChapter[] = [{ id: 123 }, { id: 456 }, { id: 42168 }];
          const subjectChapterCollection: ISubjectChapter[] = [{ id: 123 }];
          expectedResult = service.addSubjectChapterToCollectionIfMissing(subjectChapterCollection, ...subjectChapterArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const subjectChapter: ISubjectChapter = { id: 123 };
          const subjectChapter2: ISubjectChapter = { id: 456 };
          expectedResult = service.addSubjectChapterToCollectionIfMissing([], subjectChapter, subjectChapter2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(subjectChapter);
          expect(expectedResult).toContain(subjectChapter2);
        });

        it('should accept null and undefined values', () => {
          const subjectChapter: ISubjectChapter = { id: 123 };
          expectedResult = service.addSubjectChapterToCollectionIfMissing([], null, subjectChapter, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(subjectChapter);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
