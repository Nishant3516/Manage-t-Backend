import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { StudentDiscountService } from '../service/student-discount.service';

import { StudentDiscountComponent } from './student-discount.component';

describe('Component Tests', () => {
  describe('StudentDiscount Management Component', () => {
    let comp: StudentDiscountComponent;
    let fixture: ComponentFixture<StudentDiscountComponent>;
    let service: StudentDiscountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentDiscountComponent],
      })
        .overrideTemplate(StudentDiscountComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentDiscountComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(StudentDiscountService);

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
      expect(comp.studentDiscounts?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
