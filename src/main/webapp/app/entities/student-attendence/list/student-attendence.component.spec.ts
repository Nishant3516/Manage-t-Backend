import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { StudentAttendenceService } from '../service/student-attendence.service';

import { StudentAttendenceComponent } from './student-attendence.component';

describe('Component Tests', () => {
  describe('StudentAttendence Management Component', () => {
    let comp: StudentAttendenceComponent;
    let fixture: ComponentFixture<StudentAttendenceComponent>;
    let service: StudentAttendenceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentAttendenceComponent],
      })
        .overrideTemplate(StudentAttendenceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentAttendenceComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(StudentAttendenceService);

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
      expect(comp.studentAttendences?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
