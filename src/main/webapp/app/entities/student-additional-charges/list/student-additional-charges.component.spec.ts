import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { StudentAdditionalChargesService } from '../service/student-additional-charges.service';

import { StudentAdditionalChargesComponent } from './student-additional-charges.component';

describe('Component Tests', () => {
  describe('StudentAdditionalCharges Management Component', () => {
    let comp: StudentAdditionalChargesComponent;
    let fixture: ComponentFixture<StudentAdditionalChargesComponent>;
    let service: StudentAdditionalChargesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentAdditionalChargesComponent],
      })
        .overrideTemplate(StudentAdditionalChargesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentAdditionalChargesComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(StudentAdditionalChargesService);

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
      expect(comp.studentAdditionalCharges?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
