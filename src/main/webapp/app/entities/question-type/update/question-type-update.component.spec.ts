import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QuestionTypeService } from '../service/question-type.service';
import { IQuestionType, QuestionType } from '../question-type.model';
import { ITenant } from 'app/entities/tenant/tenant.model';
import { TenantService } from 'app/entities/tenant/service/tenant.service';

import { QuestionTypeUpdateComponent } from './question-type-update.component';

describe('QuestionType Management Update Component', () => {
  let comp: QuestionTypeUpdateComponent;
  let fixture: ComponentFixture<QuestionTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questionTypeService: QuestionTypeService;
  let tenantService: TenantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QuestionTypeUpdateComponent],
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
      .overrideTemplate(QuestionTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questionTypeService = TestBed.inject(QuestionTypeService);
    tenantService = TestBed.inject(TenantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tenant query and add missing value', () => {
      const questionType: IQuestionType = { id: 456 };
      const tenant: ITenant = { id: 93918 };
      questionType.tenant = tenant;

      const tenantCollection: ITenant[] = [{ id: 84389 }];
      jest.spyOn(tenantService, 'query').mockReturnValue(of(new HttpResponse({ body: tenantCollection })));
      const additionalTenants = [tenant];
      const expectedCollection: ITenant[] = [...additionalTenants, ...tenantCollection];
      jest.spyOn(tenantService, 'addTenantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ questionType });
      comp.ngOnInit();

      expect(tenantService.query).toHaveBeenCalled();
      expect(tenantService.addTenantToCollectionIfMissing).toHaveBeenCalledWith(tenantCollection, ...additionalTenants);
      expect(comp.tenantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const questionType: IQuestionType = { id: 456 };
      const tenant: ITenant = { id: 79112 };
      questionType.tenant = tenant;

      activatedRoute.data = of({ questionType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(questionType));
      expect(comp.tenantsSharedCollection).toContain(tenant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<QuestionType>>();
      const questionType = { id: 123 };
      jest.spyOn(questionTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(questionTypeService.update).toHaveBeenCalledWith(questionType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<QuestionType>>();
      const questionType = new QuestionType();
      jest.spyOn(questionTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionType }));
      saveSubject.complete();

      // THEN
      expect(questionTypeService.create).toHaveBeenCalledWith(questionType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<QuestionType>>();
      const questionType = { id: 123 };
      jest.spyOn(questionTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(questionTypeService.update).toHaveBeenCalledWith(questionType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTenantById', () => {
      it('Should return tracked Tenant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTenantById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
