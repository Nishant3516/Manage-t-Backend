jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SchoolPictureGalleryService } from '../service/school-picture-gallery.service';
import { ISchoolPictureGallery, SchoolPictureGallery } from '../school-picture-gallery.model';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

import { SchoolPictureGalleryUpdateComponent } from './school-picture-gallery-update.component';

describe('Component Tests', () => {
  describe('SchoolPictureGallery Management Update Component', () => {
    let comp: SchoolPictureGalleryUpdateComponent;
    let fixture: ComponentFixture<SchoolPictureGalleryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let schoolPictureGalleryService: SchoolPictureGalleryService;
    let schoolClassService: SchoolClassService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SchoolPictureGalleryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SchoolPictureGalleryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SchoolPictureGalleryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      schoolPictureGalleryService = TestBed.inject(SchoolPictureGalleryService);
      schoolClassService = TestBed.inject(SchoolClassService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SchoolClass query and add missing value', () => {
        const schoolPictureGallery: ISchoolPictureGallery = { id: 456 };
        const schoolClasses: ISchoolClass[] = [{ id: 51793 }];
        schoolPictureGallery.schoolClasses = schoolClasses;

        const schoolClassCollection: ISchoolClass[] = [{ id: 80832 }];
        spyOn(schoolClassService, 'query').and.returnValue(of(new HttpResponse({ body: schoolClassCollection })));
        const additionalSchoolClasses = [...schoolClasses];
        const expectedCollection: ISchoolClass[] = [...additionalSchoolClasses, ...schoolClassCollection];
        spyOn(schoolClassService, 'addSchoolClassToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ schoolPictureGallery });
        comp.ngOnInit();

        expect(schoolClassService.query).toHaveBeenCalled();
        expect(schoolClassService.addSchoolClassToCollectionIfMissing).toHaveBeenCalledWith(
          schoolClassCollection,
          ...additionalSchoolClasses
        );
        expect(comp.schoolClassesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const schoolPictureGallery: ISchoolPictureGallery = { id: 456 };
        const schoolClasses: ISchoolClass = { id: 60103 };
        schoolPictureGallery.schoolClasses = [schoolClasses];

        activatedRoute.data = of({ schoolPictureGallery });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(schoolPictureGallery));
        expect(comp.schoolClassesSharedCollection).toContain(schoolClasses);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolPictureGallery = { id: 123 };
        spyOn(schoolPictureGalleryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolPictureGallery });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: schoolPictureGallery }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(schoolPictureGalleryService.update).toHaveBeenCalledWith(schoolPictureGallery);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolPictureGallery = new SchoolPictureGallery();
        spyOn(schoolPictureGalleryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolPictureGallery });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: schoolPictureGallery }));
        saveSubject.complete();

        // THEN
        expect(schoolPictureGalleryService.create).toHaveBeenCalledWith(schoolPictureGallery);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolPictureGallery = { id: 123 };
        spyOn(schoolPictureGalleryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolPictureGallery });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(schoolPictureGalleryService.update).toHaveBeenCalledWith(schoolPictureGallery);
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
