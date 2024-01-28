jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClassClassWorkService } from '../service/class-class-work.service';
import { IClassClassWork, ClassClassWork } from '../class-class-work.model';
import { IChapterSection } from 'app/entities/chapter-section/chapter-section.model';
import { ChapterSectionService } from 'app/entities/chapter-section/service/chapter-section.service';

import { ClassClassWorkUpdateComponent } from './class-class-work-update.component';

describe('Component Tests', () => {
  describe('ClassClassWork Management Update Component', () => {
    let comp: ClassClassWorkUpdateComponent;
    let fixture: ComponentFixture<ClassClassWorkUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let classClassWorkService: ClassClassWorkService;
    let chapterSectionService: ChapterSectionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClassClassWorkUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClassClassWorkUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassClassWorkUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      classClassWorkService = TestBed.inject(ClassClassWorkService);
      chapterSectionService = TestBed.inject(ChapterSectionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ChapterSection query and add missing value', () => {
        const classClassWork: IClassClassWork = { id: 456 };
        const chapterSection: IChapterSection = { id: 61853 };
        classClassWork.chapterSection = chapterSection;

        const chapterSectionCollection: IChapterSection[] = [{ id: 94614 }];
        spyOn(chapterSectionService, 'query').and.returnValue(of(new HttpResponse({ body: chapterSectionCollection })));
        const additionalChapterSections = [chapterSection];
        const expectedCollection: IChapterSection[] = [...additionalChapterSections, ...chapterSectionCollection];
        spyOn(chapterSectionService, 'addChapterSectionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ classClassWork });
        comp.ngOnInit();

        expect(chapterSectionService.query).toHaveBeenCalled();
        expect(chapterSectionService.addChapterSectionToCollectionIfMissing).toHaveBeenCalledWith(
          chapterSectionCollection,
          ...additionalChapterSections
        );
        expect(comp.chapterSectionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const classClassWork: IClassClassWork = { id: 456 };
        const chapterSection: IChapterSection = { id: 18276 };
        classClassWork.chapterSection = chapterSection;

        activatedRoute.data = of({ classClassWork });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(classClassWork));
        expect(comp.chapterSectionsSharedCollection).toContain(chapterSection);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classClassWork = { id: 123 };
        spyOn(classClassWorkService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classClassWork });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classClassWork }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(classClassWorkService.update).toHaveBeenCalledWith(classClassWork);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classClassWork = new ClassClassWork();
        spyOn(classClassWorkService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classClassWork });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classClassWork }));
        saveSubject.complete();

        // THEN
        expect(classClassWorkService.create).toHaveBeenCalledWith(classClassWork);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classClassWork = { id: 123 };
        spyOn(classClassWorkService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classClassWork });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(classClassWorkService.update).toHaveBeenCalledWith(classClassWork);
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
