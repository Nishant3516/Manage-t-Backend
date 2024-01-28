import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StudentPaymentsDetailComponent } from './student-payments-detail.component';

describe('Component Tests', () => {
  describe('StudentPayments Management Detail Component', () => {
    let comp: StudentPaymentsDetailComponent;
    let fixture: ComponentFixture<StudentPaymentsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [StudentPaymentsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ studentPayments: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(StudentPaymentsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentPaymentsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load studentPayments on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studentPayments).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
