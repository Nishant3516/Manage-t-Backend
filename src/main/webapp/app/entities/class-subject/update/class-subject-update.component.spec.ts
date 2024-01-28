jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClassSubjectService } from '../service/class-subject.service';
import { IClassSubject, ClassSubject } from '../class-subject.model';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

import { ClassSubjectUpdateComponent } from './class-subject-update.component';

describe('Component Tests', () => {
  describe('ClassSubject Management Update Component', () => {
    let comp: ClassSubjectUpdateComponent;
    let fixture: ComponentFixture<ClassSubjectUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let classSubjectService: ClassSubjectService;
    let schoolClassService: SchoolClassService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClassSubjectUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClassSubjectUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassSubjectUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      classSubjectService = TestBed.inject(ClassSubjectService);
      schoolClassService = TestBed.inject(SchoolClassService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SchoolClass query and add missing value', () => {
        const classSubject: IClassSubject = { id: 456 };
        const schoolClasses: ISchoolClass[] = [{ id: 20341 }];
        classSubject.schoolClasses = schoolClasses;

        const schoolClassCollection: ISchoolClass[] = [{ id: 1402 }];
        spyOn(schoolClassService, 'query').and.returnValue(of(new HttpResponse({ body: schoolClassCollection })));
        const additionalSchoolClasses = [...schoolClasses];
        const expectedCollection: ISchoolClass[] = [...additionalSchoolClasses, ...schoolClassCollection];
        spyOn(schoolClassService, 'addSchoolClassToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ classSubject });
        comp.ngOnInit();

        expect(schoolClassService.query).toHaveBeenCalled();
        expect(schoolClassService.addSchoolClassToCollectionIfMissing).toHaveBeenCalledWith(
          schoolClassCollection,
          ...additionalSchoolClasses
        );
        expect(comp.schoolClassesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const classSubject: IClassSubject = { id: 456 };
        const schoolClasses: ISchoolClass = { id: 59089 };
        classSubject.schoolClasses = [schoolClasses];

        activatedRoute.data = of({ classSubject });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(classSubject));
        expect(comp.schoolClassesSharedCollection).toContain(schoolClasses);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classSubject = { id: 123 };
        spyOn(classSubjectService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classSubject });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classSubject }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(classSubjectService.update).toHaveBeenCalledWith(classSubject);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classSubject = new ClassSubject();
        spyOn(classSubjectService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classSubject });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classSubject }));
        saveSubject.complete();

        // THEN
        expect(classSubjectService.create).toHaveBeenCalledWith(classSubject);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classSubject = { id: 123 };
        spyOn(classSubjectService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classSubject });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(classSubjectService.update).toHaveBeenCalledWith(classSubject);
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
