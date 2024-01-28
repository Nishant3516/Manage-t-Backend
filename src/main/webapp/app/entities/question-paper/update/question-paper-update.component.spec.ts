import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QuestionPaperService } from '../service/question-paper.service';
import { IQuestionPaper, QuestionPaper } from '../question-paper.model';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';
import { ITenant } from 'app/entities/tenant/tenant.model';
import { TenantService } from 'app/entities/tenant/service/tenant.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { ClassSubjectService } from 'app/entities/class-subject/service/class-subject.service';

import { QuestionPaperUpdateComponent } from './question-paper-update.component';

describe('QuestionPaper Management Update Component', () => {
  let comp: QuestionPaperUpdateComponent;
  let fixture: ComponentFixture<QuestionPaperUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questionPaperService: QuestionPaperService;
  let questionService: QuestionService;
  let tagService: TagService;
  let tenantService: TenantService;
  let schoolClassService: SchoolClassService;
  let classSubjectService: ClassSubjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QuestionPaperUpdateComponent],
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
      .overrideTemplate(QuestionPaperUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionPaperUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questionPaperService = TestBed.inject(QuestionPaperService);
    questionService = TestBed.inject(QuestionService);
    tagService = TestBed.inject(TagService);
    tenantService = TestBed.inject(TenantService);
    schoolClassService = TestBed.inject(SchoolClassService);
    classSubjectService = TestBed.inject(ClassSubjectService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Question query and add missing value', () => {
      const questionPaper: IQuestionPaper = { id: 456 };
      const questions: IQuestion[] = [{ id: 27419 }];
      questionPaper.questions = questions;

      const questionCollection: IQuestion[] = [{ id: 86211 }];
      jest.spyOn(questionService, 'query').mockReturnValue(of(new HttpResponse({ body: questionCollection })));
      const additionalQuestions = [...questions];
      const expectedCollection: IQuestion[] = [...additionalQuestions, ...questionCollection];
      jest.spyOn(questionService, 'addQuestionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ questionPaper });
      comp.ngOnInit();

      expect(questionService.query).toHaveBeenCalled();
      expect(questionService.addQuestionToCollectionIfMissing).toHaveBeenCalledWith(questionCollection, ...additionalQuestions);
      expect(comp.questionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Tag query and add missing value', () => {
      const questionPaper: IQuestionPaper = { id: 456 };
      const tags: ITag[] = [{ id: 57705 }];
      questionPaper.tags = tags;

      const tagCollection: ITag[] = [{ id: 76941 }];
      jest.spyOn(tagService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCollection })));
      const additionalTags = [...tags];
      const expectedCollection: ITag[] = [...additionalTags, ...tagCollection];
      jest.spyOn(tagService, 'addTagToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ questionPaper });
      comp.ngOnInit();

      expect(tagService.query).toHaveBeenCalled();
      expect(tagService.addTagToCollectionIfMissing).toHaveBeenCalledWith(tagCollection, ...additionalTags);
      expect(comp.tagsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Tenant query and add missing value', () => {
      const questionPaper: IQuestionPaper = { id: 456 };
      const tenant: ITenant = { id: 27394 };
      questionPaper.tenant = tenant;

      const tenantCollection: ITenant[] = [{ id: 90535 }];
      jest.spyOn(tenantService, 'query').mockReturnValue(of(new HttpResponse({ body: tenantCollection })));
      const additionalTenants = [tenant];
      const expectedCollection: ITenant[] = [...additionalTenants, ...tenantCollection];
      jest.spyOn(tenantService, 'addTenantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ questionPaper });
      comp.ngOnInit();

      expect(tenantService.query).toHaveBeenCalled();
      expect(tenantService.addTenantToCollectionIfMissing).toHaveBeenCalledWith(tenantCollection, ...additionalTenants);
      expect(comp.tenantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SchoolClass query and add missing value', () => {
      const questionPaper: IQuestionPaper = { id: 456 };
      const schoolClass: ISchoolClass = { id: 41191 };
      questionPaper.schoolClass = schoolClass;

      const schoolClassCollection: ISchoolClass[] = [{ id: 97695 }];
      jest.spyOn(schoolClassService, 'query').mockReturnValue(of(new HttpResponse({ body: schoolClassCollection })));
      const additionalSchoolClasses = [schoolClass];
      const expectedCollection: ISchoolClass[] = [...additionalSchoolClasses, ...schoolClassCollection];
      jest.spyOn(schoolClassService, 'addSchoolClassToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ questionPaper });
      comp.ngOnInit();

      expect(schoolClassService.query).toHaveBeenCalled();
      expect(schoolClassService.addSchoolClassToCollectionIfMissing).toHaveBeenCalledWith(
        schoolClassCollection,
        ...additionalSchoolClasses
      );
      expect(comp.schoolClassesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ClassSubject query and add missing value', () => {
      const questionPaper: IQuestionPaper = { id: 456 };
      const classSubject: IClassSubject = { id: 81118 };
      questionPaper.classSubject = classSubject;

      const classSubjectCollection: IClassSubject[] = [{ id: 8220 }];
      jest.spyOn(classSubjectService, 'query').mockReturnValue(of(new HttpResponse({ body: classSubjectCollection })));
      const additionalClassSubjects = [classSubject];
      const expectedCollection: IClassSubject[] = [...additionalClassSubjects, ...classSubjectCollection];
      jest.spyOn(classSubjectService, 'addClassSubjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ questionPaper });
      comp.ngOnInit();

      expect(classSubjectService.query).toHaveBeenCalled();
      expect(classSubjectService.addClassSubjectToCollectionIfMissing).toHaveBeenCalledWith(
        classSubjectCollection,
        ...additionalClassSubjects
      );
      expect(comp.classSubjectsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const questionPaper: IQuestionPaper = { id: 456 };
      const questions: IQuestion = { id: 60511 };
      questionPaper.questions = [questions];
      const tags: ITag = { id: 2139 };
      questionPaper.tags = [tags];
      const tenant: ITenant = { id: 67615 };
      questionPaper.tenant = tenant;
      const schoolClass: ISchoolClass = { id: 60621 };
      questionPaper.schoolClass = schoolClass;
      const classSubject: IClassSubject = { id: 50892 };
      questionPaper.classSubject = classSubject;

      activatedRoute.data = of({ questionPaper });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(questionPaper));
      expect(comp.questionsSharedCollection).toContain(questions);
      expect(comp.tagsSharedCollection).toContain(tags);
      expect(comp.tenantsSharedCollection).toContain(tenant);
      expect(comp.schoolClassesSharedCollection).toContain(schoolClass);
      expect(comp.classSubjectsSharedCollection).toContain(classSubject);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<QuestionPaper>>();
      const questionPaper = { id: 123 };
      jest.spyOn(questionPaperService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionPaper });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionPaper }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(questionPaperService.update).toHaveBeenCalledWith(questionPaper);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<QuestionPaper>>();
      const questionPaper = new QuestionPaper();
      jest.spyOn(questionPaperService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionPaper });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionPaper }));
      saveSubject.complete();

      // THEN
      expect(questionPaperService.create).toHaveBeenCalledWith(questionPaper);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<QuestionPaper>>();
      const questionPaper = { id: 123 };
      jest.spyOn(questionPaperService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionPaper });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(questionPaperService.update).toHaveBeenCalledWith(questionPaper);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackQuestionById', () => {
      it('Should return tracked Question primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackQuestionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTagById', () => {
      it('Should return tracked Tag primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTagById(0, entity);
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
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedQuestion', () => {
      it('Should return option if no Question is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedQuestion(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Question for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedQuestion(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Question is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedQuestion(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

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
