import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QuestionService } from '../service/question.service';
import { IQuestion, Question } from '../question.model';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';
import { IQuestionType } from 'app/entities/question-type/question-type.model';
import { QuestionTypeService } from 'app/entities/question-type/service/question-type.service';
import { ITenant } from 'app/entities/tenant/tenant.model';
import { TenantService } from 'app/entities/tenant/service/tenant.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { ClassSubjectService } from 'app/entities/class-subject/service/class-subject.service';
import { ISubjectChapter } from 'app/entities/subject-chapter/subject-chapter.model';
import { SubjectChapterService } from 'app/entities/subject-chapter/service/subject-chapter.service';

import { QuestionUpdateComponent } from './question-update.component';

describe('Question Management Update Component', () => {
  let comp: QuestionUpdateComponent;
  let fixture: ComponentFixture<QuestionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questionService: QuestionService;
  let tagService: TagService;
  let questionTypeService: QuestionTypeService;
  let tenantService: TenantService;
  let schoolClassService: SchoolClassService;
  let classSubjectService: ClassSubjectService;
  let subjectChapterService: SubjectChapterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QuestionUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(QuestionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questionService = TestBed.inject(QuestionService);
    tagService = TestBed.inject(TagService);
    questionTypeService = TestBed.inject(QuestionTypeService);
    tenantService = TestBed.inject(TenantService);
    schoolClassService = TestBed.inject(SchoolClassService);
    classSubjectService = TestBed.inject(ClassSubjectService);
    subjectChapterService = TestBed.inject(SubjectChapterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tag query and add missing value', () => {
      const question: IQuestion = { id: 456 };
      const tags: ITag[] = [{ id: 4067 }];
      question.tags = tags;

      const tagCollection: ITag[] = [{ id: 24495 }];
      jest.spyOn(tagService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCollection })));
      const additionalTags = [...tags];
      const expectedCollection: ITag[] = [...additionalTags, ...tagCollection];
      jest.spyOn(tagService, 'addTagToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(tagService.query).toHaveBeenCalled();
      expect(tagService.addTagToCollectionIfMissing).toHaveBeenCalledWith(tagCollection, ...additionalTags);
      expect(comp.tagsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call QuestionType query and add missing value', () => {
      const question: IQuestion = { id: 456 };
      const questionType: IQuestionType = { id: 38666 };
      question.questionType = questionType;

      const questionTypeCollection: IQuestionType[] = [{ id: 48615 }];
      jest.spyOn(questionTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: questionTypeCollection })));
      const additionalQuestionTypes = [questionType];
      const expectedCollection: IQuestionType[] = [...additionalQuestionTypes, ...questionTypeCollection];
      jest.spyOn(questionTypeService, 'addQuestionTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(questionTypeService.query).toHaveBeenCalled();
      expect(questionTypeService.addQuestionTypeToCollectionIfMissing).toHaveBeenCalledWith(
        questionTypeCollection,
        ...additionalQuestionTypes
      );
      expect(comp.questionTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Tenant query and add missing value', () => {
      const question: IQuestion = { id: 456 };
      const tenant: ITenant = { id: 58454 };
      question.tenant = tenant;

      const tenantCollection: ITenant[] = [{ id: 58390 }];
      jest.spyOn(tenantService, 'query').mockReturnValue(of(new HttpResponse({ body: tenantCollection })));
      const additionalTenants = [tenant];
      const expectedCollection: ITenant[] = [...additionalTenants, ...tenantCollection];
      jest.spyOn(tenantService, 'addTenantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(tenantService.query).toHaveBeenCalled();
      expect(tenantService.addTenantToCollectionIfMissing).toHaveBeenCalledWith(tenantCollection, ...additionalTenants);
      expect(comp.tenantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SchoolClass query and add missing value', () => {
      const question: IQuestion = { id: 456 };
      const schoolClass: ISchoolClass = { id: 41035 };
      question.schoolClass = schoolClass;

      const schoolClassCollection: ISchoolClass[] = [{ id: 72486 }];
      jest.spyOn(schoolClassService, 'query').mockReturnValue(of(new HttpResponse({ body: schoolClassCollection })));
      const additionalSchoolClasses = [schoolClass];
      const expectedCollection: ISchoolClass[] = [...additionalSchoolClasses, ...schoolClassCollection];
      jest.spyOn(schoolClassService, 'addSchoolClassToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(schoolClassService.query).toHaveBeenCalled();
      expect(schoolClassService.addSchoolClassToCollectionIfMissing).toHaveBeenCalledWith(
        schoolClassCollection,
        ...additionalSchoolClasses
      );
      expect(comp.schoolClassesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ClassSubject query and add missing value', () => {
      const question: IQuestion = { id: 456 };
      const classSubject: IClassSubject = { id: 27927 };
      question.classSubject = classSubject;

      const classSubjectCollection: IClassSubject[] = [{ id: 92130 }];
      jest.spyOn(classSubjectService, 'query').mockReturnValue(of(new HttpResponse({ body: classSubjectCollection })));
      const additionalClassSubjects = [classSubject];
      const expectedCollection: IClassSubject[] = [...additionalClassSubjects, ...classSubjectCollection];
      jest.spyOn(classSubjectService, 'addClassSubjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(classSubjectService.query).toHaveBeenCalled();
      expect(classSubjectService.addClassSubjectToCollectionIfMissing).toHaveBeenCalledWith(
        classSubjectCollection,
        ...additionalClassSubjects
      );
      expect(comp.classSubjectsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SubjectChapter query and add missing value', () => {
      const question: IQuestion = { id: 456 };
      const subjectChapter: ISubjectChapter = { id: 51419 };
      question.subjectChapter = subjectChapter;

      const subjectChapterCollection: ISubjectChapter[] = [{ id: 33594 }];
      jest.spyOn(subjectChapterService, 'query').mockReturnValue(of(new HttpResponse({ body: subjectChapterCollection })));
      const additionalSubjectChapters = [subjectChapter];
      const expectedCollection: ISubjectChapter[] = [...additionalSubjectChapters, ...subjectChapterCollection];
      jest.spyOn(subjectChapterService, 'addSubjectChapterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(subjectChapterService.query).toHaveBeenCalled();
      expect(subjectChapterService.addSubjectChapterToCollectionIfMissing).toHaveBeenCalledWith(
        subjectChapterCollection,
        ...additionalSubjectChapters
      );
      expect(comp.subjectChaptersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const question: IQuestion = { id: 456 };
      const tags: ITag = { id: 97758 };
      question.tags = [tags];
      const questionType: IQuestionType = { id: 6851 };
      question.questionType = questionType;
      const tenant: ITenant = { id: 46413 };
      question.tenant = tenant;
      const schoolClass: ISchoolClass = { id: 66915 };
      question.schoolClass = schoolClass;
      const classSubject: IClassSubject = { id: 75769 };
      question.classSubject = classSubject;
      const subjectChapter: ISubjectChapter = { id: 96715 };
      question.subjectChapter = subjectChapter;

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(question));
      expect(comp.tagsSharedCollection).toContain(tags);
      expect(comp.questionTypesSharedCollection).toContain(questionType);
      expect(comp.tenantsSharedCollection).toContain(tenant);
      expect(comp.schoolClassesSharedCollection).toContain(schoolClass);
      expect(comp.classSubjectsSharedCollection).toContain(classSubject);
      expect(comp.subjectChaptersSharedCollection).toContain(subjectChapter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Question>>();
      const question = { id: 123 };
      jest.spyOn(questionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ question });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: question }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(questionService.update).toHaveBeenCalledWith(question);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Question>>();
      const question = new Question();
      jest.spyOn(questionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ question });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: question }));
      saveSubject.complete();

      // THEN
      expect(questionService.create).toHaveBeenCalledWith(question);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Question>>();
      const question = { id: 123 };
      jest.spyOn(questionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ question });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(questionService.update).toHaveBeenCalledWith(question);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTagById', () => {
      it('Should return tracked Tag primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTagById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackQuestionTypeById', () => {
      it('Should return tracked QuestionType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackQuestionTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTenantById', () => {
      it('Should return tracked Tenant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTenantById(0, entity);
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

  describe('Getting selected relationships', () => {
    describe('getSelectedTag', () => {
      it('Should return option if no Tag is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedTag(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Tag for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedTag(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Tag is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedTag(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
