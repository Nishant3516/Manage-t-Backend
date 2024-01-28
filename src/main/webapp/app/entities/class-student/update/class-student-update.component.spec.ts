jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClassStudentService } from '../service/class-student.service';
import { IClassStudent, ClassStudent } from '../class-student.model';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

import { ClassStudentUpdateComponent } from './class-student-update.component';

describe('Component Tests', () => {
  describe('ClassStudent Management Update Component', () => {
    let comp: ClassStudentUpdateComponent;
    let fixture: ComponentFixture<ClassStudentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let classStudentService: ClassStudentService;
    let schoolClassService: SchoolClassService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClassStudentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClassStudentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassStudentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      classStudentService = TestBed.inject(ClassStudentService);
      schoolClassService = TestBed.inject(SchoolClassService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SchoolClass query and add missing value', () => {
        const classStudent: IClassStudent = { id: 456 };
        const schoolClass: ISchoolClass = { id: 13000 };
        classStudent.schoolClass = schoolClass;

        const schoolClassCollection: ISchoolClass[] = [{ id: 99455 }];
        spyOn(schoolClassService, 'query').and.returnValue(of(new HttpResponse({ body: schoolClassCollection })));
        const additionalSchoolClasses = [schoolClass];
        const expectedCollection: ISchoolClass[] = [...additionalSchoolClasses, ...schoolClassCollection];
        spyOn(schoolClassService, 'addSchoolClassToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ classStudent });
        comp.ngOnInit();

        expect(schoolClassService.query).toHaveBeenCalled();
        expect(schoolClassService.addSchoolClassToCollectionIfMissing).toHaveBeenCalledWith(
          schoolClassCollection,
          ...additionalSchoolClasses
        );
        expect(comp.schoolClassesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const classStudent: IClassStudent = { id: 456 };
        const schoolClass: ISchoolClass = { id: 56023 };
        classStudent.schoolClass = schoolClass;

        activatedRoute.data = of({ classStudent });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(classStudent));
        expect(comp.schoolClassesSharedCollection).toContain(schoolClass);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classStudent = { id: 123 };
        spyOn(classStudentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classStudent });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classStudent }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(classStudentService.update).toHaveBeenCalledWith(classStudent);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classStudent = new ClassStudent();
        spyOn(classStudentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classStudent });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classStudent }));
        saveSubject.complete();

        // THEN
        expect(classStudentService.create).toHaveBeenCalledWith(classStudent);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classStudent = { id: 123 };
        spyOn(classStudentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classStudent });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(classStudentService.update).toHaveBeenCalledWith(classStudent);
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
    });
  });
});
