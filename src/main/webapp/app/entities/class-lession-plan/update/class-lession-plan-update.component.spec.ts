jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClassLessionPlanService } from '../service/class-lession-plan.service';
import { IClassLessionPlan, ClassLessionPlan } from '../class-lession-plan.model';
import { IChapterSection } from 'app/entities/chapter-section/chapter-section.model';
import { ChapterSectionService } from 'app/entities/chapter-section/service/chapter-section.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { ClassSubjectService } from 'app/entities/class-subject/service/class-subject.service';
import { ISubjectChapter } from 'app/entities/subject-chapter/subject-chapter.model';
import { SubjectChapterService } from 'app/entities/subject-chapter/service/subject-chapter.service';

import { ClassLessionPlanUpdateComponent } from './class-lession-plan-update.component';

describe('Component Tests', () => {
  describe('ClassLessionPlan Management Update Component', () => {
    let comp: ClassLessionPlanUpdateComponent;
    let fixture: ComponentFixture<ClassLessionPlanUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let classLessionPlanService: ClassLessionPlanService;
    let chapterSectionService: ChapterSectionService;
    let schoolClassService: SchoolClassService;
    let classSubjectService: ClassSubjectService;
    let subjectChapterService: SubjectChapterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClassLessionPlanUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClassLessionPlanUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassLessionPlanUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      classLessionPlanService = TestBed.inject(ClassLessionPlanService);
      chapterSectionService = TestBed.inject(ChapterSectionService);
      schoolClassService = TestBed.inject(SchoolClassService);
      classSubjectService = TestBed.inject(ClassSubjectService);
      subjectChapterService = TestBed.inject(SubjectChapterService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ChapterSection query and add missing value', () => {
        const classLessionPlan: IClassLessionPlan = { id: 456 };
        const chapterSection: IChapterSection = { id: 90476 };
        classLessionPlan.chapterSection = chapterSection;

        const chapterSectionCollection: IChapterSection[] = [{ id: 806 }];
        spyOn(chapterSectionService, 'query').and.returnValue(of(new HttpResponse({ body: chapterSectionCollection })));
        const additionalChapterSections = [chapterSection];
        const expectedCollection: IChapterSection[] = [...additionalChapterSections, ...chapterSectionCollection];
        spyOn(chapterSectionService, 'addChapterSectionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ classLessionPlan });
        comp.ngOnInit();

        expect(chapterSectionService.query).toHaveBeenCalled();
        expect(chapterSectionService.addChapterSectionToCollectionIfMissing).toHaveBeenCalledWith(
          chapterSectionCollection,
          ...additionalChapterSections
        );
        expect(comp.chapterSectionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call SchoolClass query and add missing value', () => {
        const classLessionPlan: IClassLessionPlan = { id: 456 };
        const schoolClass: ISchoolClass = { id: 33513 };
        classLessionPlan.schoolClass = schoolClass;

        const schoolClassCollection: ISchoolClass[] = [{ id: 42743 }];
        spyOn(schoolClassService, 'query').and.returnValue(of(new HttpResponse({ body: schoolClassCollection })));
        const additionalSchoolClasses = [schoolClass];
        const expectedCollection: ISchoolClass[] = [...additionalSchoolClasses, ...schoolClassCollection];
        spyOn(schoolClassService, 'addSchoolClassToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ classLessionPlan });
        comp.ngOnInit();

        expect(schoolClassService.query).toHaveBeenCalled();
        expect(schoolClassService.addSchoolClassToCollectionIfMissing).toHaveBeenCalledWith(
          schoolClassCollection,
          ...additionalSchoolClasses
        );
        expect(comp.schoolClassesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ClassSubject query and add missing value', () => {
        const classLessionPlan: IClassLessionPlan = { id: 456 };
        const classSubject: IClassSubject = { id: 58105 };
        classLessionPlan.classSubject = classSubject;

        const classSubjectCollection: IClassSubject[] = [{ id: 48018 }];
        spyOn(classSubjectService, 'query').and.returnValue(of(new HttpResponse({ body: classSubjectCollection })));
        const additionalClassSubjects = [classSubject];
        const expectedCollection: IClassSubject[] = [...additionalClassSubjects, ...classSubjectCollection];
        spyOn(classSubjectService, 'addClassSubjectToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ classLessionPlan });
        comp.ngOnInit();

        expect(classSubjectService.query).toHaveBeenCalled();
        expect(classSubjectService.addClassSubjectToCollectionIfMissing).toHaveBeenCalledWith(
          classSubjectCollection,
          ...additionalClassSubjects
        );
        expect(comp.classSubjectsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call SubjectChapter query and add missing value', () => {
        const classLessionPlan: IClassLessionPlan = { id: 456 };
        const subjectChapter: ISubjectChapter = { id: 48039 };
        classLessionPlan.subjectChapter = subjectChapter;

        const subjectChapterCollection: ISubjectChapter[] = [{ id: 78434 }];
        spyOn(subjectChapterService, 'query').and.returnValue(of(new HttpResponse({ body: subjectChapterCollection })));
        const additionalSubjectChapters = [subjectChapter];
        const expectedCollection: ISubjectChapter[] = [...additionalSubjectChapters, ...subjectChapterCollection];
        spyOn(subjectChapterService, 'addSubjectChapterToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ classLessionPlan });
        comp.ngOnInit();

        expect(subjectChapterService.query).toHaveBeenCalled();
        expect(subjectChapterService.addSubjectChapterToCollectionIfMissing).toHaveBeenCalledWith(
          subjectChapterCollection,
          ...additionalSubjectChapters
        );
        expect(comp.subjectChaptersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const classLessionPlan: IClassLessionPlan = { id: 456 };
        const chapterSection: IChapterSection = { id: 66454 };
        classLessionPlan.chapterSection = chapterSection;
        const schoolClass: ISchoolClass = { id: 77123 };
        classLessionPlan.schoolClass = schoolClass;
        const classSubject: IClassSubject = { id: 70372 };
        classLessionPlan.classSubject = classSubject;
        const subjectChapter: ISubjectChapter = { id: 98789 };
        classLessionPlan.subjectChapter = subjectChapter;

        activatedRoute.data = of({ classLessionPlan });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(classLessionPlan));
        expect(comp.chapterSectionsSharedCollection).toContain(chapterSection);
        expect(comp.schoolClassesSharedCollection).toContain(schoolClass);
        expect(comp.classSubjectsSharedCollection).toContain(classSubject);
        expect(comp.subjectChaptersSharedCollection).toContain(subjectChapter);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classLessionPlan = { id: 123 };
        spyOn(classLessionPlanService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classLessionPlan });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classLessionPlan }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(classLessionPlanService.update).toHaveBeenCalledWith(classLessionPlan);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classLessionPlan = new ClassLessionPlan();
        spyOn(classLessionPlanService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classLessionPlan });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classLessionPlan }));
        saveSubject.complete();

        // THEN
        expect(classLessionPlanService.create).toHaveBeenCalledWith(classLessionPlan);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classLessionPlan = { id: 123 };
        spyOn(classLessionPlanService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classLessionPlan });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(classLessionPlanService.update).toHaveBeenCalledWith(classLessionPlan);
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

      describe('trackSchoolClassById', () => {
        it('Should return tracked SchoolClass primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSchoolClassById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackClassSubjectById', () => {
        it('Should return tracked ClassSubject primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClassSubjectById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

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
