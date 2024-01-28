jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SchoolDaysOffService } from '../service/school-days-off.service';
import { ISchoolDaysOff, SchoolDaysOff } from '../school-days-off.model';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

import { SchoolDaysOffUpdateComponent } from './school-days-off-update.component';

describe('Component Tests', () => {
  describe('SchoolDaysOff Management Update Component', () => {
    let comp: SchoolDaysOffUpdateComponent;
    let fixture: ComponentFixture<SchoolDaysOffUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let schoolDaysOffService: SchoolDaysOffService;
    let schoolClassService: SchoolClassService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SchoolDaysOffUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SchoolDaysOffUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SchoolDaysOffUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      schoolDaysOffService = TestBed.inject(SchoolDaysOffService);
      schoolClassService = TestBed.inject(SchoolClassService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SchoolClass query and add missing value', () => {
        const schoolDaysOff: ISchoolDaysOff = { id: 456 };
        const schoolClasses: ISchoolClass[] = [{ id: 80088 }];
        schoolDaysOff.schoolClasses = schoolClasses;

        const schoolClassCollection: ISchoolClass[] = [{ id: 91206 }];
        spyOn(schoolClassService, 'query').and.returnValue(of(new HttpResponse({ body: schoolClassCollection })));
        const additionalSchoolClasses = [...schoolClasses];
        const expectedCollection: ISchoolClass[] = [...additionalSchoolClasses, ...schoolClassCollection];
        spyOn(schoolClassService, 'addSchoolClassToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ schoolDaysOff });
        comp.ngOnInit();

        expect(schoolClassService.query).toHaveBeenCalled();
        expect(schoolClassService.addSchoolClassToCollectionIfMissing).toHaveBeenCalledWith(
          schoolClassCollection,
          ...additionalSchoolClasses
        );
        expect(comp.schoolClassesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const schoolDaysOff: ISchoolDaysOff = { id: 456 };
        const schoolClasses: ISchoolClass = { id: 3079 };
        schoolDaysOff.schoolClasses = [schoolClasses];

        activatedRoute.data = of({ schoolDaysOff });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(schoolDaysOff));
        expect(comp.schoolClassesSharedCollection).toContain(schoolClasses);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolDaysOff = { id: 123 };
        spyOn(schoolDaysOffService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolDaysOff });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: schoolDaysOff }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(schoolDaysOffService.update).toHaveBeenCalledWith(schoolDaysOff);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolDaysOff = new SchoolDaysOff();
        spyOn(schoolDaysOffService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolDaysOff });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: schoolDaysOff }));
        saveSubject.complete();

        // THEN
        expect(schoolDaysOffService.create).toHaveBeenCalledWith(schoolDaysOff);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolDaysOff = { id: 123 };
        spyOn(schoolDaysOffService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolDaysOff });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(schoolDaysOffService.update).toHaveBeenCalledWith(schoolDaysOff);
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
