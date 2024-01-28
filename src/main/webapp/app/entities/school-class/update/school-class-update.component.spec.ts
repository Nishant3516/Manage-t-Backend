jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SchoolClassService } from '../service/school-class.service';
import { ISchoolClass, SchoolClass } from '../school-class.model';
import { ISchool } from 'app/entities/school/school.model';
import { SchoolService } from 'app/entities/school/service/school.service';

import { SchoolClassUpdateComponent } from './school-class-update.component';

describe('Component Tests', () => {
  describe('SchoolClass Management Update Component', () => {
    let comp: SchoolClassUpdateComponent;
    let fixture: ComponentFixture<SchoolClassUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let schoolClassService: SchoolClassService;
    let schoolService: SchoolService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SchoolClassUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SchoolClassUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SchoolClassUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      schoolClassService = TestBed.inject(SchoolClassService);
      schoolService = TestBed.inject(SchoolService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call School query and add missing value', () => {
        const schoolClass: ISchoolClass = { id: 456 };
        const school: ISchool = { id: 31924 };
        schoolClass.school = school;

        const schoolCollection: ISchool[] = [{ id: 65862 }];
        spyOn(schoolService, 'query').and.returnValue(of(new HttpResponse({ body: schoolCollection })));
        const additionalSchools = [school];
        const expectedCollection: ISchool[] = [...additionalSchools, ...schoolCollection];
        spyOn(schoolService, 'addSchoolToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ schoolClass });
        comp.ngOnInit();

        expect(schoolService.query).toHaveBeenCalled();
        expect(schoolService.addSchoolToCollectionIfMissing).toHaveBeenCalledWith(schoolCollection, ...additionalSchools);
        expect(comp.schoolsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const schoolClass: ISchoolClass = { id: 456 };
        const school: ISchool = { id: 61930 };
        schoolClass.school = school;

        activatedRoute.data = of({ schoolClass });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(schoolClass));
        expect(comp.schoolsSharedCollection).toContain(school);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolClass = { id: 123 };
        spyOn(schoolClassService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolClass });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: schoolClass }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(schoolClassService.update).toHaveBeenCalledWith(schoolClass);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolClass = new SchoolClass();
        spyOn(schoolClassService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolClass });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: schoolClass }));
        saveSubject.complete();

        // THEN
        expect(schoolClassService.create).toHaveBeenCalledWith(schoolClass);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolClass = { id: 123 };
        spyOn(schoolClassService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolClass });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(schoolClassService.update).toHaveBeenCalledWith(schoolClass);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSchoolById', () => {
        it('Should return tracked School primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSchoolById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
