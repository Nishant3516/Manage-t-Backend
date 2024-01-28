jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ChapterSectionService } from '../service/chapter-section.service';
import { IChapterSection, ChapterSection } from '../chapter-section.model';
import { ISubjectChapter } from 'app/entities/subject-chapter/subject-chapter.model';
import { SubjectChapterService } from 'app/entities/subject-chapter/service/subject-chapter.service';

import { ChapterSectionUpdateComponent } from './chapter-section-update.component';

describe('Component Tests', () => {
  describe('ChapterSection Management Update Component', () => {
    let comp: ChapterSectionUpdateComponent;
    let fixture: ComponentFixture<ChapterSectionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let chapterSectionService: ChapterSectionService;
    let subjectChapterService: SubjectChapterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ChapterSectionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ChapterSectionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChapterSectionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      chapterSectionService = TestBed.inject(ChapterSectionService);
      subjectChapterService = TestBed.inject(SubjectChapterService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SubjectChapter query and add missing value', () => {
        const chapterSection: IChapterSection = { id: 456 };
        const subjectChapter: ISubjectChapter = { id: 64090 };
        chapterSection.subjectChapter = subjectChapter;

        const subjectChapterCollection: ISubjectChapter[] = [{ id: 90956 }];
        spyOn(subjectChapterService, 'query').and.returnValue(of(new HttpResponse({ body: subjectChapterCollection })));
        const additionalSubjectChapters = [subjectChapter];
        const expectedCollection: ISubjectChapter[] = [...additionalSubjectChapters, ...subjectChapterCollection];
        spyOn(subjectChapterService, 'addSubjectChapterToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ chapterSection });
        comp.ngOnInit();

        expect(subjectChapterService.query).toHaveBeenCalled();
        expect(subjectChapterService.addSubjectChapterToCollectionIfMissing).toHaveBeenCalledWith(
          subjectChapterCollection,
          ...additionalSubjectChapters
        );
        expect(comp.subjectChaptersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const chapterSection: IChapterSection = { id: 456 };
        const subjectChapter: ISubjectChapter = { id: 62949 };
        chapterSection.subjectChapter = subjectChapter;

        activatedRoute.data = of({ chapterSection });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(chapterSection));
        expect(comp.subjectChaptersSharedCollection).toContain(subjectChapter);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const chapterSection = { id: 123 };
        spyOn(chapterSectionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ chapterSection });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: chapterSection }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(chapterSectionService.update).toHaveBeenCalledWith(chapterSection);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const chapterSection = new ChapterSection();
        spyOn(chapterSectionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ chapterSection });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: chapterSection }));
        saveSubject.complete();

        // THEN
        expect(chapterSectionService.create).toHaveBeenCalledWith(chapterSection);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const chapterSection = { id: 123 };
        spyOn(chapterSectionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ chapterSection });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(chapterSectionService.update).toHaveBeenCalledWith(chapterSection);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSubjectChapterById', () => {
        it('Should return tracked SubjectChapter primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSubjectChapterById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
