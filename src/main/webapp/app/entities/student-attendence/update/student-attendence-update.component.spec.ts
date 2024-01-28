jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StudentAttendenceService } from '../service/student-attendence.service';
import { IStudentAttendence, StudentAttendence } from '../student-attendence.model';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';

import { StudentAttendenceUpdateComponent } from './student-attendence-update.component';

describe('Component Tests', () => {
  describe('StudentAttendence Management Update Component', () => {
    let comp: StudentAttendenceUpdateComponent;
    let fixture: ComponentFixture<StudentAttendenceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let studentAttendenceService: StudentAttendenceService;
    let classStudentService: ClassStudentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentAttendenceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(StudentAttendenceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentAttendenceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      studentAttendenceService = TestBed.inject(StudentAttendenceService);
      classStudentService = TestBed.inject(ClassStudentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ClassStudent query and add missing value', () => {
        const studentAttendence: IStudentAttendence = { id: 456 };
        const classStudent: IClassStudent = { id: 23544 };
        studentAttendence.classStudent = classStudent;

        const classStudentCollection: IClassStudent[] = [{ id: 37216 }];
        spyOn(classStudentService, 'query').and.returnValue(of(new HttpResponse({ body: classStudentCollection })));
        const additionalClassStudents = [classStudent];
        const expectedCollection: IClassStudent[] = [...additionalClassStudents, ...classStudentCollection];
        spyOn(classStudentService, 'addClassStudentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ studentAttendence });
        comp.ngOnInit();

        expect(classStudentService.query).toHaveBeenCalled();
        expect(classStudentService.addClassStudentToCollectionIfMissing).toHaveBeenCalledWith(
          classStudentCollection,
          ...additionalClassStudents
        );
        expect(comp.classStudentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const studentAttendence: IStudentAttendence = { id: 456 };
        const classStudent: IClassStudent = { id: 44260 };
        studentAttendence.classStudent = classStudent;

        activatedRoute.data = of({ studentAttendence });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(studentAttendence));
        expect(comp.classStudentsSharedCollection).toContain(classStudent);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentAttendence = { id: 123 };
        spyOn(studentAttendenceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentAttendence });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: studentAttendence }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(studentAttendenceService.update).toHaveBeenCalledWith(studentAttendence);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentAttendence = new StudentAttendence();
        spyOn(studentAttendenceService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentAttendence });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: studentAttendence }));
        saveSubject.complete();

        // THEN
        expect(studentAttendenceService.create).toHaveBeenCalledWith(studentAttendence);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentAttendence = { id: 123 };
        spyOn(studentAttendenceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentAttendence });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(studentAttendenceService.update).toHaveBeenCalledWith(studentAttendence);
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
    });
  });
});
