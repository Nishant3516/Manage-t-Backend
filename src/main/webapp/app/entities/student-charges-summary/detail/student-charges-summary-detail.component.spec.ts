import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StudentChargesSummaryDetailComponent } from './student-charges-summary-detail.component';

describe('Component Tests', () => {
  describe('StudentChargesSummary Management Detail Component', () => {
    let comp: StudentChargesSummaryDetailComponent;
    let fixture: ComponentFixture<StudentChargesSummaryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [StudentChargesSummaryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ studentChargesSummary: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(StudentChargesSummaryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentChargesSummaryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load studentChargesSummary on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studentChargesSummary).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
