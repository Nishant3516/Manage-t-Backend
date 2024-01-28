import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ClassFeeService } from '../service/class-fee.service';

import { ClassFeeComponent } from './class-fee.component';

describe('Component Tests', () => {
  describe('ClassFee Management Component', () => {
    let comp: ClassFeeComponent;
    let fixture: ComponentFixture<ClassFeeComponent>;
    let service: ClassFeeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClassFeeComponent],
      })
        .overrideTemplate(ClassFeeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassFeeComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ClassFeeService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.classFees?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
