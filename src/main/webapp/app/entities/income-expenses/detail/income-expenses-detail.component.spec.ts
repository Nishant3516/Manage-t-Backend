import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IncomeExpensesDetailComponent } from './income-expenses-detail.component';

describe('IncomeExpenses Management Detail Component', () => {
  let comp: IncomeExpensesDetailComponent;
  let fixture: ComponentFixture<IncomeExpensesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IncomeExpensesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ incomeExpenses: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IncomeExpensesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IncomeExpensesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load incomeExpenses on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.incomeExpenses).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
