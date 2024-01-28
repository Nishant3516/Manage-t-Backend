jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClassFeeService } from '../service/class-fee.service';
import { IClassFee, ClassFee } from '../class-fee.model';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { SchoolLedgerHeadService } from 'app/entities/school-ledger-head/service/school-ledger-head.service';

import { ClassFeeUpdateComponent } from './class-fee-update.component';

describe('Component Tests', () => {
  describe('ClassFee Management Update Component', () => {
    let comp: ClassFeeUpdateComponent;
    let fixture: ComponentFixture<ClassFeeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let classFeeService: ClassFeeService;
    let schoolClassService: SchoolClassService;
    let schoolLedgerHeadService: SchoolLedgerHeadService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClassFeeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClassFeeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassFeeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      classFeeService = TestBed.inject(ClassFeeService);
      schoolClassService = TestBed.inject(SchoolClassService);
      schoolLedgerHeadService = TestBed.inject(SchoolLedgerHeadService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SchoolClass query and add missing value', () => {
        const classFee: IClassFee = { id: 456 };
        const schoolClasses: ISchoolClass[] = [{ id: 75901 }];
        classFee.schoolClasses = schoolClasses;

        const schoolClassCollection: ISchoolClass[] = [{ id: 76590 }];
        spyOn(schoolClassService, 'query').and.returnValue(of(new HttpResponse({ body: schoolClassCollection })));
        const additionalSchoolClasses = [...schoolClasses];
        const expectedCollection: ISchoolClass[] = [...additionalSchoolClasses, ...schoolClassCollection];
        spyOn(schoolClassService, 'addSchoolClassToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ classFee });
        comp.ngOnInit();

        expect(schoolClassService.query).toHaveBeenCalled();
        expect(schoolClassService.addSchoolClassToCollectionIfMissing).toHaveBeenCalledWith(
          schoolClassCollection,
          ...additionalSchoolClasses
        );
        expect(comp.schoolClassesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call SchoolLedgerHead query and add missing value', () => {
        const classFee: IClassFee = { id: 456 };
        const schoolLedgerHead: ISchoolLedgerHead = { id: 46535 };
        classFee.schoolLedgerHead = schoolLedgerHead;

        const schoolLedgerHeadCollection: ISchoolLedgerHead[] = [{ id: 39662 }];
        spyOn(schoolLedgerHeadService, 'query').and.returnValue(of(new HttpResponse({ body: schoolLedgerHeadCollection })));
        const additionalSchoolLedgerHeads = [schoolLedgerHead];
        const expectedCollection: ISchoolLedgerHead[] = [...additionalSchoolLedgerHeads, ...schoolLedgerHeadCollection];
        spyOn(schoolLedgerHeadService, 'addSchoolLedgerHeadToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ classFee });
        comp.ngOnInit();

        expect(schoolLedgerHeadService.query).toHaveBeenCalled();
        expect(schoolLedgerHeadService.addSchoolLedgerHeadToCollectionIfMissing).toHaveBeenCalledWith(
          schoolLedgerHeadCollection,
          ...additionalSchoolLedgerHeads
        );
        expect(comp.schoolLedgerHeadsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const classFee: IClassFee = { id: 456 };
        const schoolClasses: ISchoolClass = { id: 23776 };
        classFee.schoolClasses = [schoolClasses];
        const schoolLedgerHead: ISchoolLedgerHead = { id: 75116 };
        classFee.schoolLedgerHead = schoolLedgerHead;

        activatedRoute.data = of({ classFee });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(classFee));
        expect(comp.schoolClassesSharedCollection).toContain(schoolClasses);
        expect(comp.schoolLedgerHeadsSharedCollection).toContain(schoolLedgerHead);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classFee = { id: 123 };
        spyOn(classFeeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classFee });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classFee }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(classFeeService.update).toHaveBeenCalledWith(classFee);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classFee = new ClassFee();
        spyOn(classFeeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classFee });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classFee }));
        saveSubject.complete();

        // THEN
        expect(classFeeService.create).toHaveBeenCalledWith(classFee);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classFee = { id: 123 };
        spyOn(classFeeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classFee });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(classFeeService.update).toHaveBeenCalledWith(classFee);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSchoolClassById', () => {
        it('Should return tracked SchoolClass primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSchoolClassById(0, entity);
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

    describe('Getting selected relationships', () => {
      describe('getSelectedSchoolClass', () => {
        it('Should return option if no SchoolClass is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedSchoolClass(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected SchoolClass for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedSchoolClass(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this SchoolClass is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedSchoolClass(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
