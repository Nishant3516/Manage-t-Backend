jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StudentAdditionalChargesService } from '../service/student-additional-charges.service';
import { IStudentAdditionalCharges, StudentAdditionalCharges } from '../student-additional-charges.model';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { SchoolLedgerHeadService } from 'app/entities/school-ledger-head/service/school-ledger-head.service';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';

import { StudentAdditionalChargesUpdateComponent } from './student-additional-charges-update.component';

describe('Component Tests', () => {
  describe('StudentAdditionalCharges Management Update Component', () => {
    let comp: StudentAdditionalChargesUpdateComponent;
    let fixture: ComponentFixture<StudentAdditionalChargesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let studentAdditionalChargesService: StudentAdditionalChargesService;
    let schoolLedgerHeadService: SchoolLedgerHeadService;
    let classStudentService: ClassStudentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentAdditionalChargesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(StudentAdditionalChargesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentAdditionalChargesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      studentAdditionalChargesService = TestBed.inject(StudentAdditionalChargesService);
      schoolLedgerHeadService = TestBed.inject(SchoolLedgerHeadService);
      classStudentService = TestBed.inject(ClassStudentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SchoolLedgerHead query and add missing value', () => {
        const studentAdditionalCharges: IStudentAdditionalCharges = { id: 456 };
        const schoolLedgerHead: ISchoolLedgerHead = { id: 27578 };
        studentAdditionalCharges.schoolLedgerHead = schoolLedgerHead;

        const schoolLedgerHeadCollection: ISchoolLedgerHead[] = [{ id: 64433 }];
        spyOn(schoolLedgerHeadService, 'query').and.returnValue(of(new HttpResponse({ body: schoolLedgerHeadCollection })));
        const additionalSchoolLedgerHeads = [schoolLedgerHead];
        const expectedCollection: ISchoolLedgerHead[] = [...additionalSchoolLedgerHeads, ...schoolLedgerHeadCollection];
        spyOn(schoolLedgerHeadService, 'addSchoolLedgerHeadToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ studentAdditionalCharges });
        comp.ngOnInit();

        expect(schoolLedgerHeadService.query).toHaveBeenCalled();
        expect(schoolLedgerHeadService.addSchoolLedgerHeadToCollectionIfMissing).toHaveBeenCalledWith(
          schoolLedgerHeadCollection,
          ...additionalSchoolLedgerHeads
        );
        expect(comp.schoolLedgerHeadsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ClassStudent query and add missing value', () => {
        const studentAdditionalCharges: IStudentAdditionalCharges = { id: 456 };
        const classStudent: IClassStudent = { id: 21249 };
        studentAdditionalCharges.classStudent = classStudent;

        const classStudentCollection: IClassStudent[] = [{ id: 72658 }];
        spyOn(classStudentService, 'query').and.returnValue(of(new HttpResponse({ body: classStudentCollection })));
        const additionalClassStudents = [classStudent];
        const expectedCollection: IClassStudent[] = [...additionalClassStudents, ...classStudentCollection];
        spyOn(classStudentService, 'addClassStudentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ studentAdditionalCharges });
        comp.ngOnInit();

        expect(classStudentService.query).toHaveBeenCalled();
        expect(classStudentService.addClassStudentToCollectionIfMissing).toHaveBeenCalledWith(
          classStudentCollection,
          ...additionalClassStudents
        );
        expect(comp.classStudentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const studentAdditionalCharges: IStudentAdditionalCharges = { id: 456 };
        const schoolLedgerHead: ISchoolLedgerHead = { id: 90952 };
        studentAdditionalCharges.schoolLedgerHead = schoolLedgerHead;
        const classStudent: IClassStudent = { id: 69324 };
        studentAdditionalCharges.classStudent = classStudent;

        activatedRoute.data = of({ studentAdditionalCharges });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(studentAdditionalCharges));
        expect(comp.schoolLedgerHeadsSharedCollection).toContain(schoolLedgerHead);
        expect(comp.classStudentsSharedCollection).toContain(classStudent);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentAdditionalCharges = { id: 123 };
        spyOn(studentAdditionalChargesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentAdditionalCharges });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: studentAdditionalCharges }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(studentAdditionalChargesService.update).toHaveBeenCalledWith(studentAdditionalCharges);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentAdditionalCharges = new StudentAdditionalCharges();
        spyOn(studentAdditionalChargesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentAdditionalCharges });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: studentAdditionalCharges }));
        saveSubject.complete();

        // THEN
        expect(studentAdditionalChargesService.create).toHaveBeenCalledWith(studentAdditionalCharges);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentAdditionalCharges = { id: 123 };
        spyOn(studentAdditionalChargesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentAdditionalCharges });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(studentAdditionalChargesService.update).toHaveBeenCalledWith(studentAdditionalCharges);
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
