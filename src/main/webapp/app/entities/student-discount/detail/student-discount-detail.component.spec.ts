import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StudentDiscountDetailComponent } from './student-discount-detail.component';

describe('Component Tests', () => {
  describe('StudentDiscount Management Detail Component', () => {
    let comp: StudentDiscountDetailComponent;
    let fixture: ComponentFixture<StudentDiscountDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [StudentDiscountDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ studentDiscount: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(StudentDiscountDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentDiscountDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load studentDiscount on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studentDiscount).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
