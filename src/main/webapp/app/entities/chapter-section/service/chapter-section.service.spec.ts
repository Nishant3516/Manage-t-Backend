import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IChapterSection, ChapterSection } from '../chapter-section.model';

import { ChapterSectionService } from './chapter-section.service';

describe('Service Tests', () => {
  describe('ChapterSection Service', () => {
    let service: ChapterSectionService;
    let httpMock: HttpTestingController;
    let elemDefault: IChapterSection;
    let expectedResult: IChapterSection | IChapterSection[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ChapterSectionService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        sectionName: 'AAAAAAA',
        sectionNumber: 'AAAAAAA',
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

      it('should create a ChapterSection', () => {
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

        service.create(new ChapterSection()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ChapterSection', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sectionName: 'BBBBBB',
            sectionNumber: 'BBBBBB',
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

      it('should partial update a ChapterSection', () => {
        const patchObject = Object.assign(
          {
            sectionName: 'BBBBBB',
            sectionNumber: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
          },
          new ChapterSection()
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

      it('should return a list of ChapterSection', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sectionName: 'BBBBBB',
            sectionNumber: 'BBBBBB',
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

      it('should delete a ChapterSection', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addChapterSectionToCollectionIfMissing', () => {
        it('should add a ChapterSection to an empty array', () => {
          const chapterSection: IChapterSection = { id: 123 };
          expectedResult = service.addChapterSectionToCollectionIfMissing([], chapterSection);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chapterSection);
        });

        it('should not add a ChapterSection to an array that contains it', () => {
          const chapterSection: IChapterSection = { id: 123 };
          const chapterSectionCollection: IChapterSection[] = [
            {
              ...chapterSection,
            },
            { id: 456 },
          ];
          expectedResult = service.addChapterSectionToCollectionIfMissing(chapterSectionCollection, chapterSection);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ChapterSection to an array that doesn't contain it", () => {
          const chapterSection: IChapterSection = { id: 123 };
          const chapterSectionCollection: IChapterSection[] = [{ id: 456 }];
          expectedResult = service.addChapterSectionToCollectionIfMissing(chapterSectionCollection, chapterSection);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chapterSection);
        });

        it('should add only unique ChapterSection to an array', () => {
          const chapterSectionArray: IChapterSection[] = [{ id: 123 }, { id: 456 }, { id: 22922 }];
          const chapterSectionCollection: IChapterSection[] = [{ id: 123 }];
          expectedResult = service.addChapterSectionToCollectionIfMissing(chapterSectionCollection, ...chapterSectionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const chapterSection: IChapterSection = { id: 123 };
          const chapterSection2: IChapterSection = { id: 456 };
          expectedResult = service.addChapterSectionToCollectionIfMissing([], chapterSection, chapterSection2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chapterSection);
          expect(expectedResult).toContain(chapterSection2);
        });

        it('should accept null and undefined values', () => {
          const chapterSection: IChapterSection = { id: 123 };
          expectedResult = service.addChapterSectionToCollectionIfMissing([], null, chapterSection, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chapterSection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
