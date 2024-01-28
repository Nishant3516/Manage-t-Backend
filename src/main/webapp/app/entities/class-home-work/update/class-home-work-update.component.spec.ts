jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClassHomeWorkService } from '../service/class-home-work.service';
import { IClassHomeWork, ClassHomeWork } from '../class-home-work.model';
import { IChapterSection } from 'app/entities/chapter-section/chapter-section.model';
import { ChapterSectionService } from 'app/entities/chapter-section/service/chapter-section.service';

import { ClassHomeWorkUpdateComponent } from './class-home-work-update.component';

describe('Component Tests', () => {
  describe('ClassHomeWork Management Update Component', () => {
    let comp: ClassHomeWorkUpdateComponent;
    let fixture: ComponentFixture<ClassHomeWorkUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let classHomeWorkService: ClassHomeWorkService;
    let chapterSectionService: ChapterSectionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClassHomeWorkUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClassHomeWorkUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassHomeWorkUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      classHomeWorkService = TestBed.inject(ClassHomeWorkService);
      chapterSectionService = TestBed.inject(ChapterSectionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ChapterSection query and add missing value', () => {
        const classHomeWork: IClassHomeWork = { id: 456 };
        const chapterSection: IChapterSection = { id: 66352 };
        classHomeWork.chapterSection = chapterSection;

        const chapterSectionCollection: IChapterSection[] = [{ id: 18861 }];
        spyOn(chapterSectionService, 'query').and.returnValue(of(new HttpResponse({ body: chapterSectionCollection })));
        const additionalChapterSections = [chapterSection];
        const expectedCollection: IChapterSection[] = [...additionalChapterSections, ...chapterSectionCollection];
        spyOn(chapterSectionService, 'addChapterSectionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ classHomeWork });
        comp.ngOnInit();

        expect(chapterSectionService.query).toHaveBeenCalled();
        expect(chapterSectionService.addChapterSectionToCollectionIfMissing).toHaveBeenCalledWith(
          chapterSectionCollection,
          ...additionalChapterSections
        );
        expect(comp.chapterSectionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const classHomeWork: IClassHomeWork = { id: 456 };
        const chapterSection: IChapterSection = { id: 5749 };
        classHomeWork.chapterSection = chapterSection;

        activatedRoute.data = of({ classHomeWork });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(classHomeWork));
        expect(comp.chapterSectionsSharedCollection).toContain(chapterSection);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classHomeWork = { id: 123 };
        spyOn(classHomeWorkService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classHomeWork });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classHomeWork }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(classHomeWorkService.update).toHaveBeenCalledWith(classHomeWork);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classHomeWork = new ClassHomeWork();
        spyOn(classHomeWorkService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classHomeWork });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classHomeWork }));
        saveSubject.complete();

        // THEN
        expect(classHomeWorkService.create).toHaveBeenCalledWith(classHomeWork);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classHomeWork = { id: 123 };
        spyOn(classHomeWorkService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classHomeWork });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(classHomeWorkService.update).toHaveBeenCalledWith(classHomeWork);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackChapterSectionById', () => {
        it('Should return tracked ChapterSection primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackChapterSectionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
