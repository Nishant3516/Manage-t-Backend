import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VendorsService } from '../service/vendors.service';
import { IVendors, Vendors } from '../vendors.model';

import { VendorsUpdateComponent } from './vendors-update.component';

describe('Vendors Management Update Component', () => {
  let comp: VendorsUpdateComponent;
  let fixture: ComponentFixture<VendorsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vendorsService: VendorsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VendorsUpdateComponent],
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
      .overrideTemplate(VendorsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VendorsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vendorsService = TestBed.inject(VendorsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vendors: IVendors = { id: 456 };

      activatedRoute.data = of({ vendors });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(vendors));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vendors>>();
      const vendors = { id: 123 };
      jest.spyOn(vendorsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vendors });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vendors }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(vendorsService.update).toHaveBeenCalledWith(vendors);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vendors>>();
      const vendors = new Vendors();
      jest.spyOn(vendorsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vendors });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vendors }));
      saveSubject.complete();

      // THEN
      expect(vendorsService.create).toHaveBeenCalledWith(vendors);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vendors>>();
      const vendors = { id: 123 };
      jest.spyOn(vendorsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vendors });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vendorsService.update).toHaveBeenCalledWith(vendors);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
