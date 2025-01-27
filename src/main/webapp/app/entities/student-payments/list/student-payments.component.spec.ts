import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { StudentPaymentsService } from '../service/student-payments.service';

import { StudentPaymentsComponent } from './student-payments.component';

describe('Component Tests', () => {
  describe('StudentPayments Management Component', () => {
    let comp: StudentPaymentsComponent;
    let fixture: ComponentFixture<StudentPaymentsComponent>;
    let service: StudentPaymentsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentPaymentsComponent],
      })
        .overrideTemplate(StudentPaymentsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentPaymentsComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(StudentPaymentsService);

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
      expect(comp.studentPayments?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
