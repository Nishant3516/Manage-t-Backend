jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StudentDiscountService } from '../service/student-discount.service';
import { IStudentDiscount, StudentDiscount } from '../student-discount.model';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { SchoolLedgerHeadService } from 'app/entities/school-ledger-head/service/school-ledger-head.service';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';

import { StudentDiscountUpdateComponent } from './student-discount-update.component';

describe('Component Tests', () => {
  describe('StudentDiscount Management Update Component', () => {
    let comp: StudentDiscountUpdateComponent;
    let fixture: ComponentFixture<StudentDiscountUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let studentDiscountService: StudentDiscountService;
    let schoolLedgerHeadService: SchoolLedgerHeadService;
    let classStudentService: ClassStudentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentDiscountUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(StudentDiscountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentDiscountUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      studentDiscountService = TestBed.inject(StudentDiscountService);
      schoolLedgerHeadService = TestBed.inject(SchoolLedgerHeadService);
      classStudentService = TestBed.inject(ClassStudentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SchoolLedgerHead query and add missing value', () => {
        const studentDiscount: IStudentDiscount = { id: 456 };
        const schoolLedgerHead: ISchoolLedgerHead = { id: 45550 };
        studentDiscount.schoolLedgerHead = schoolLedgerHead;

        const schoolLedgerHeadCollection: ISchoolLedgerHead[] = [{ id: 30362 }];
        spyOn(schoolLedgerHeadService, 'query').and.returnValue(of(new HttpResponse({ body: schoolLedgerHeadCollection })));
        const additionalSchoolLedgerHeads = [schoolLedgerHead];
        const expectedCollection: ISchoolLedgerHead[] = [...additionalSchoolLedgerHeads, ...schoolLedgerHeadCollection];
        spyOn(schoolLedgerHeadService, 'addSchoolLedgerHeadToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ studentDiscount });
        comp.ngOnInit();

        expect(schoolLedgerHeadService.query).toHaveBeenCalled();
        expect(schoolLedgerHeadService.addSchoolLedgerHeadToCollectionIfMissing).toHaveBeenCalledWith(
          schoolLedgerHeadCollection,
          ...additionalSchoolLedgerHeads
        );
        expect(comp.schoolLedgerHeadsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ClassStudent query and add missing value', () => {
        const studentDiscount: IStudentDiscount = { id: 456 };
        const classStudent: IClassStudent = { id: 8022 };
        studentDiscount.classStudent = classStudent;

        const classStudentCollection: IClassStudent[] = [{ id: 61433 }];
        spyOn(classStudentService, 'query').and.returnValue(of(new HttpResponse({ body: classStudentCollection })));
        const additionalClassStudents = [classStudent];
        const expectedCollection: IClassStudent[] = [...additionalClassStudents, ...classStudentCollection];
        spyOn(classStudentService, 'addClassStudentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ studentDiscount });
        comp.ngOnInit();

        expect(classStudentService.query).toHaveBeenCalled();
        expect(classStudentService.addClassStudentToCollectionIfMissing).toHaveBeenCalledWith(
          classStudentCollection,
          ...additionalClassStudents
        );
        expect(comp.classStudentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const studentDiscount: IStudentDiscount = { id: 456 };
        const schoolLedgerHead: ISchoolLedgerHead = { id: 39370 };
        studentDiscount.schoolLedgerHead = schoolLedgerHead;
        const classStudent: IClassStudent = { id: 9301 };
        studentDiscount.classStudent = classStudent;

        activatedRoute.data = of({ studentDiscount });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(studentDiscount));
        expect(comp.schoolLedgerHeadsSharedCollection).toContain(schoolLedgerHead);
        expect(comp.classStudentsSharedCollection).toContain(classStudent);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentDiscount = { id: 123 };
        spyOn(studentDiscountService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentDiscount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: studentDiscount }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(studentDiscountService.update).toHaveBeenCalledWith(studentDiscount);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentDiscount = new StudentDiscount();
        spyOn(studentDiscountService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentDiscount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: studentDiscount }));
        saveSubject.complete();

        // THEN
        expect(studentDiscountService.create).toHaveBeenCalledWith(studentDiscount);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentDiscount = { id: 123 };
        spyOn(studentDiscountService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentDiscount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(studentDiscountService.update).toHaveBeenCalledWith(studentDiscount);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSchoolLedgerHeadById', () => {
        it('Should return tracked SchoolLedgerHead primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSchoolLedgerHeadById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackClassStudentById', () => {
        it('Should return tracked ClassStudent primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClassStudentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
