import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { STRouteService } from '../service/st-route.service';
import { ISTRoute, STRoute } from '../st-route.model';

import { STRouteUpdateComponent } from './st-route-update.component';

describe('STRoute Management Update Component', () => {
  let comp: STRouteUpdateComponent;
  let fixture: ComponentFixture<STRouteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sTRouteService: STRouteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [STRouteUpdateComponent],
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
      .overrideTemplate(STRouteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(STRouteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sTRouteService = TestBed.inject(STRouteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sTRoute: ISTRoute = { id: 456 };

      activatedRoute.data = of({ sTRoute });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sTRoute));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<STRoute>>();
      const sTRoute = { id: 123 };
      jest.spyOn(sTRouteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sTRoute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sTRoute }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sTRouteService.update).toHaveBeenCalledWith(sTRoute);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<STRoute>>();
      const sTRoute = new STRoute();
      jest.spyOn(sTRouteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sTRoute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sTRoute }));
      saveSubject.complete();

      // THEN
      expect(sTRouteService.create).toHaveBeenCalledWith(sTRoute);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<STRoute>>();
      const sTRoute = { id: 123 };
      jest.spyOn(sTRouteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sTRoute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sTRouteService.update).toHaveBeenCalledWith(sTRoute);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
