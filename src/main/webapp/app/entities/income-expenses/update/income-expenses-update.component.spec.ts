import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IncomeExpensesService } from '../service/income-expenses.service';
import { IIncomeExpenses, IncomeExpenses } from '../income-expenses.model';
import { IVendors } from 'app/entities/vendors/vendors.model';
import { VendorsService } from 'app/entities/vendors/service/vendors.service';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { SchoolLedgerHeadService } from 'app/entities/school-ledger-head/service/school-ledger-head.service';

import { IncomeExpensesUpdateComponent } from './income-expenses-update.component';

describe('IncomeExpenses Management Update Component', () => {
  let comp: IncomeExpensesUpdateComponent;
  let fixture: ComponentFixture<IncomeExpensesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let incomeExpensesService: IncomeExpensesService;
  let vendorsService: VendorsService;
  let schoolLedgerHeadService: SchoolLedgerHeadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IncomeExpensesUpdateComponent],
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
      .overrideTemplate(IncomeExpensesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IncomeExpensesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    incomeExpensesService = TestBed.inject(IncomeExpensesService);
    vendorsService = TestBed.inject(VendorsService);
    schoolLedgerHeadService = TestBed.inject(SchoolLedgerHeadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Vendors query and add missing value', () => {
      const incomeExpenses: IIncomeExpenses = { id: 456 };
      const vendor: IVendors = { id: 9 };
      incomeExpenses.vendor = vendor;

      const vendorsCollection: IVendors[] = [{ id: 79699 }];
      jest.spyOn(vendorsService, 'query').mockReturnValue(of(new HttpResponse({ body: vendorsCollection })));
      const additionalVendors = [vendor];
      const expectedCollection: IVendors[] = [...additionalVendors, ...vendorsCollection];
      jest.spyOn(vendorsService, 'addVendorsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ incomeExpenses });
      comp.ngOnInit();

      expect(vendorsService.query).toHaveBeenCalled();
      expect(vendorsService.addVendorsToCollectionIfMissing).toHaveBeenCalledWith(vendorsCollection, ...additionalVendors);
      expect(comp.vendorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SchoolLedgerHead query and add missing value', () => {
      const incomeExpenses: IIncomeExpenses = { id: 456 };
      const ledgerHead: ISchoolLedgerHead = { id: 3531 };
      incomeExpenses.ledgerHead = ledgerHead;

      const schoolLedgerHeadCollection: ISchoolLedgerHead[] = [{ id: 56788 }];
      jest.spyOn(schoolLedgerHeadService, 'query').mockReturnValue(of(new HttpResponse({ body: schoolLedgerHeadCollection })));
      const additionalSchoolLedgerHeads = [ledgerHead];
      const expectedCollection: ISchoolLedgerHead[] = [...additionalSchoolLedgerHeads, ...schoolLedgerHeadCollection];
      jest.spyOn(schoolLedgerHeadService, 'addSchoolLedgerHeadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ incomeExpenses });
      comp.ngOnInit();

      expect(schoolLedgerHeadService.query).toHaveBeenCalled();
      expect(schoolLedgerHeadService.addSchoolLedgerHeadToCollectionIfMissing).toHaveBeenCalledWith(
        schoolLedgerHeadCollection,
        ...additionalSchoolLedgerHeads
      );
      expect(comp.schoolLedgerHeadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const incomeExpenses: IIncomeExpenses = { id: 456 };
      const vendor: IVendors = { id: 77055 };
      incomeExpenses.vendor = vendor;
      const ledgerHead: ISchoolLedgerHead = { id: 90728 };
      incomeExpenses.ledgerHead = ledgerHead;

      activatedRoute.data = of({ incomeExpenses });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(incomeExpenses));
      expect(comp.vendorsSharedCollection).toContain(vendor);
      expect(comp.schoolLedgerHeadsSharedCollection).toContain(ledgerHead);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IncomeExpenses>>();
      const incomeExpenses = { id: 123 };
      jest.spyOn(incomeExpensesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomeExpenses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incomeExpenses }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(incomeExpensesService.update).toHaveBeenCalledWith(incomeExpenses);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IncomeExpenses>>();
      const incomeExpenses = new IncomeExpenses();
      jest.spyOn(incomeExpensesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomeExpenses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incomeExpenses }));
      saveSubject.complete();

      // THEN
      expect(incomeExpensesService.create).toHaveBeenCalledWith(incomeExpenses);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IncomeExpenses>>();
      const incomeExpenses = { id: 123 };
      jest.spyOn(incomeExpensesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomeExpenses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(incomeExpensesService.update).toHaveBeenCalledWith(incomeExpenses);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackVendorsById', () => {
      it('Should return tracked Vendors primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVendorsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSchoolLedgerHeadById', () => {
      it('Should return tracked SchoolLedgerHead primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSchoolLedgerHeadById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
