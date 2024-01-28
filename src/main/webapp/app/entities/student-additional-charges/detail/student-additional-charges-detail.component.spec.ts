import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StudentAdditionalChargesDetailComponent } from './student-additional-charges-detail.component';

describe('Component Tests', () => {
  describe('StudentAdditionalCharges Management Detail Component', () => {
    let comp: StudentAdditionalChargesDetailComponent;
    let fixture: ComponentFixture<StudentAdditionalChargesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [StudentAdditionalChargesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ studentAdditionalCharges: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(StudentAdditionalChargesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentAdditionalChargesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load studentAdditionalCharges on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studentAdditionalCharges).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
