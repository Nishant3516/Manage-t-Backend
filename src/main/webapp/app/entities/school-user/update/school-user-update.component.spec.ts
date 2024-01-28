jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SchoolUserService } from '../service/school-user.service';
import { ISchoolUser, SchoolUser } from '../school-user.model';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { ClassSubjectService } from 'app/entities/class-subject/service/class-subject.service';

import { SchoolUserUpdateComponent } from './school-user-update.component';

describe('Component Tests', () => {
  describe('SchoolUser Management Update Component', () => {
    let comp: SchoolUserUpdateComponent;
    let fixture: ComponentFixture<SchoolUserUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let schoolUserService: SchoolUserService;
    let schoolClassService: SchoolClassService;
    let classSubjectService: ClassSubjectService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SchoolUserUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SchoolUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SchoolUserUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      schoolUserService = TestBed.inject(SchoolUserService);
      schoolClassService = TestBed.inject(SchoolClassService);
      classSubjectService = TestBed.inject(ClassSubjectService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SchoolClass query and add missing value', () => {
        const schoolUser: ISchoolUser = { id: 456 };
        const schoolClasses: ISchoolClass[] = [{ id: 57909 }];
        schoolUser.schoolClasses = schoolClasses;

        const schoolClassCollection: ISchoolClass[] = [{ id: 22792 }];
        spyOn(schoolClassService, 'query').and.returnValue(of(new HttpResponse({ body: schoolClassCollection })));
        const additionalSchoolClasses = [...schoolClasses];
        const expectedCollection: ISchoolClass[] = [...additionalSchoolClasses, ...schoolClassCollection];
        spyOn(schoolClassService, 'addSchoolClassToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ schoolUser });
        comp.ngOnInit();

        expect(schoolClassService.query).toHaveBeenCalled();
        expect(schoolClassService.addSchoolClassToCollectionIfMissing).toHaveBeenCalledWith(
          schoolClassCollection,
          ...additionalSchoolClasses
        );
        expect(comp.schoolClassesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ClassSubject query and add missing value', () => {
        const schoolUser: ISchoolUser = { id: 456 };
        const classSubjects: IClassSubject[] = [{ id: 80296 }];
        schoolUser.classSubjects = classSubjects;

        const classSubjectCollection: IClassSubject[] = [{ id: 35712 }];
        spyOn(classSubjectService, 'query').and.returnValue(of(new HttpResponse({ body: classSubjectCollection })));
        const additionalClassSubjects = [...classSubjects];
        const expectedCollection: IClassSubject[] = [...additionalClassSubjects, ...classSubjectCollection];
        spyOn(classSubjectService, 'addClassSubjectToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ schoolUser });
        comp.ngOnInit();

        expect(classSubjectService.query).toHaveBeenCalled();
        expect(classSubjectService.addClassSubjectToCollectionIfMissing).toHaveBeenCalledWith(
          classSubjectCollection,
          ...additionalClassSubjects
        );
        expect(comp.classSubjectsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const schoolUser: ISchoolUser = { id: 456 };
        const schoolClasses: ISchoolClass = { id: 65574 };
        schoolUser.schoolClasses = [schoolClasses];
        const classSubjects: IClassSubject = { id: 90923 };
        schoolUser.classSubjects = [classSubjects];

        activatedRoute.data = of({ schoolUser });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(schoolUser));
        expect(comp.schoolClassesSharedCollection).toContain(schoolClasses);
        expect(comp.classSubjectsSharedCollection).toContain(classSubjects);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolUser = { id: 123 };
        spyOn(schoolUserService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: schoolUser }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(schoolUserService.update).toHaveBeenCalledWith(schoolUser);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolUser = new SchoolUser();
        spyOn(schoolUserService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: schoolUser }));
        saveSubject.complete();

        // THEN
        expect(schoolUserService.create).toHaveBeenCalledWith(schoolUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolUser = { id: 123 };
        spyOn(schoolUserService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(schoolUserService.update).toHaveBeenCalledWith(schoolUser);
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

      describe('trackClassSubjectById', () => {
        it('Should return tracked ClassSubject primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClassSubjectById(0, entity);
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

      describe('getSelectedClassSubject', () => {
        it('Should return option if no ClassSubject is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedClassSubject(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected ClassSubject for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedClassSubject(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this ClassSubject is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedClassSubject(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
