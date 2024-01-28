import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { STIncomeExpensesService } from '../service/st-income-expenses.service';
import { ISTIncomeExpenses, STIncomeExpenses } from '../st-income-expenses.model';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';
import { ISTRoute } from 'app/entities/st-route/st-route.model';
import { STRouteService } from 'app/entities/st-route/service/st-route.service';
import { IVendors } from 'app/entities/vendors/vendors.model';
import { VendorsService } from 'app/entities/vendors/service/vendors.service';

import { STIncomeExpensesUpdateComponent } from './st-income-expenses-update.component';

describe('STIncomeExpenses Management Update Component', () => {
  let comp: STIncomeExpensesUpdateComponent;
  let fixture: ComponentFixture<STIncomeExpensesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sTIncomeExpensesService: STIncomeExpensesService;
  let classStudentService: ClassStudentService;
  let sTRouteService: STRouteService;
  let vendorsService: VendorsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [STIncomeExpensesUpdateComponent],
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
      .overrideTemplate(STIncomeExpensesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(STIncomeExpensesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sTIncomeExpensesService = TestBed.inject(STIncomeExpensesService);
    classStudentService = TestBed.inject(ClassStudentService);
    sTRouteService = TestBed.inject(STRouteService);
    vendorsService = TestBed.inject(VendorsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ClassStudent query and add missing value', () => {
      const sTIncomeExpenses: ISTIncomeExpenses = { id: 456 };
      const classStudent: IClassStudent = { id: 63728 };
      sTIncomeExpenses.classStudent = classStudent;

      const classStudentCollection: IClassStudent[] = [{ id: 94715 }];
      jest.spyOn(classStudentService, 'query').mockReturnValue(of(new HttpResponse({ body: classStudentCollection })));
      const additionalClassStudents = [classStudent];
      const expectedCollection: IClassStudent[] = [...additionalClassStudents, ...classStudentCollection];
      jest.spyOn(classStudentService, 'addClassStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sTIncomeExpenses });
      comp.ngOnInit();

      expect(classStudentService.query).toHaveBeenCalled();
      expect(classStudentService.addClassStudentToCollectionIfMissing).toHaveBeenCalledWith(
        classStudentCollection,
        ...additionalClassStudents
      );
      expect(comp.classStudentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call STRoute query and add missing value', () => {
      const sTIncomeExpenses: ISTIncomeExpenses = { id: 456 };
      const stRoute: ISTRoute = { id: 55479 };
      sTIncomeExpenses.stRoute = stRoute;

      const sTRouteCollection: ISTRoute[] = [{ id: 98539 }];
      jest.spyOn(sTRouteService, 'query').mockReturnValue(of(new HttpResponse({ body: sTRouteCollection })));
      const additionalSTRoutes = [stRoute];
      const expectedCollection: ISTRoute[] = [...additionalSTRoutes, ...sTRouteCollection];
      jest.spyOn(sTRouteService, 'addSTRouteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sTIncomeExpenses });
      comp.ngOnInit();

      expect(sTRouteService.query).toHaveBeenCalled();
      expect(sTRouteService.addSTRouteToCollectionIfMissing).toHaveBeenCalledWith(sTRouteCollection, ...additionalSTRoutes);
      expect(comp.sTRoutesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Vendors query and add missing value', () => {
      const sTIncomeExpenses: ISTIncomeExpenses = { id: 456 };
      const operatedBy: IVendors = { id: 68604 };
      sTIncomeExpenses.operatedBy = operatedBy;

      const vendorsCollection: IVendors[] = [{ id: 31716 }];
      jest.spyOn(vendorsService, 'query').mockReturnValue(of(new HttpResponse({ body: vendorsCollection })));
      const additionalVendors = [operatedBy];
      const expectedCollection: IVendors[] = [...additionalVendors, ...vendorsCollection];
      jest.spyOn(vendorsService, 'addVendorsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sTIncomeExpenses });
      comp.ngOnInit();

      expect(vendorsService.query).toHaveBeenCalled();
      expect(vendorsService.addVendorsToCollectionIfMissing).toHaveBeenCalledWith(vendorsCollection, ...additionalVendors);
      expect(comp.vendorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sTIncomeExpenses: ISTIncomeExpenses = { id: 456 };
      const classStudent: IClassStudent = { id: 57865 };
      sTIncomeExpenses.classStudent = classStudent;
      const stRoute: ISTRoute = { id: 15361 };
      sTIncomeExpenses.stRoute = stRoute;
      const operatedBy: IVendors = { id: 61107 };
      sTIncomeExpenses.operatedBy = operatedBy;

      activatedRoute.data = of({ sTIncomeExpenses });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sTIncomeExpenses));
      expect(comp.classStudentsSharedCollection).toContain(classStudent);
      expect(comp.sTRoutesSharedCollection).toContain(stRoute);
      expect(comp.vendorsSharedCollection).toContain(operatedBy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<STIncomeExpenses>>();
      const sTIncomeExpenses = { id: 123 };
      jest.spyOn(sTIncomeExpensesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sTIncomeExpenses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sTIncomeExpenses }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sTIncomeExpensesService.update).toHaveBeenCalledWith(sTIncomeExpenses);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<STIncomeExpenses>>();
      const sTIncomeExpenses = new STIncomeExpenses();
      jest.spyOn(sTIncomeExpensesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sTIncomeExpenses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sTIncomeExpenses }));
      saveSubject.complete();

      // THEN
      expect(sTIncomeExpensesService.create).toHaveBeenCalledWith(sTIncomeExpenses);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<STIncomeExpenses>>();
      const sTIncomeExpenses = { id: 123 };
      jest.spyOn(sTIncomeExpensesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sTIncomeExpenses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sTIncomeExpensesService.update).toHaveBeenCalledWith(sTIncomeExpenses);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackClassStudentById', () => {
      it('Should return tracked ClassStudent primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClassStudentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSTRouteById', () => {
      it('Should return tracked STRoute primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSTRouteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackVendorsById', () => {
      it('Should return tracked Vendors primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVendorsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
