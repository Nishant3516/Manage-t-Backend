import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SchoolReportService } from '../service/school-report.service';
import { ISchoolReport, SchoolReport } from '../school-report.model';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

import { SchoolReportUpdateComponent } from './school-report-update.component';

describe('SchoolReport Management Update Component', () => {
  let comp: SchoolReportUpdateComponent;
  let fixture: ComponentFixture<SchoolReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let schoolReportService: SchoolReportService;
  let schoolClassService: SchoolClassService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SchoolReportUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SchoolReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SchoolReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    schoolReportService = TestBed.inject(SchoolReportService);
    schoolClassService = TestBed.inject(SchoolClassService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SchoolClass query and add missing value', () => {
      const schoolReport: ISchoolReport = { id: 456 };
      const schoolClasses: ISchoolClass[] = [{ id: 74289 }];
      schoolReport.schoolClasses = schoolClasses;

      const schoolClassCollection: ISchoolClass[] = [{ id: 49263 }];
      jest.spyOn(schoolClassService, 'query').mockReturnValue(of(new HttpResponse({ body: schoolClassCollection })));
      const additionalSchoolClasses = [...schoolClasses];
      const expectedCollection: ISchoolClass[] = [...additionalSchoolClasses, ...schoolClassCollection];
      jest.spyOn(schoolClassService, 'addSchoolClassToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ schoolReport });
      comp.ngOnInit();

      expect(schoolClassService.query).toHaveBeenCalled();
      expect(schoolClassService.addSchoolClassToCollectionIfMissing).toHaveBeenCalledWith(
        schoolClassCollection,
        ...additionalSchoolClasses
      );
      expect(comp.schoolClassesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const schoolReport: ISchoolReport = { id: 456 };
      const schoolClasses: ISchoolClass = { id: 615 };
      schoolReport.schoolClasses = [schoolClasses];

      activatedRoute.data = of({ schoolReport });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(schoolReport));
      expect(comp.schoolClassesSharedCollection).toContain(schoolClasses);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SchoolReport>>();
      const schoolReport = { id: 123 };
      jest.spyOn(schoolReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ schoolReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: schoolReport }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(schoolReportService.update).toHaveBeenCalledWith(schoolReport);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SchoolReport>>();
      const schoolReport = new SchoolReport();
      jest.spyOn(schoolReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ schoolReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: schoolReport }));
      saveSubject.complete();

      // THEN
      expect(schoolReportService.create).toHaveBeenCalledWith(schoolReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SchoolReport>>();
      const schoolReport = { id: 123 };
      jest.spyOn(schoolReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ schoolReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(schoolReportService.update).toHaveBeenCalledWith(schoolReport);
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
