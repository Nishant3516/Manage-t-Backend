import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StudentAttendenceDetailComponent } from './student-attendence-detail.component';

describe('Component Tests', () => {
  describe('StudentAttendence Management Detail Component', () => {
    let comp: StudentAttendenceDetailComponent;
    let fixture: ComponentFixture<StudentAttendenceDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [StudentAttendenceDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ studentAttendence: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(StudentAttendenceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentAttendenceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load studentAttendence on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studentAttendence).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
