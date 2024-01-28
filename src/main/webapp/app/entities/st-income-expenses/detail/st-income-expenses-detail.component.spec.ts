import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { STIncomeExpensesDetailComponent } from './st-income-expenses-detail.component';

describe('STIncomeExpenses Management Detail Component', () => {
  let comp: STIncomeExpensesDetailComponent;
  let fixture: ComponentFixture<STIncomeExpensesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [STIncomeExpensesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sTIncomeExpenses: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(STIncomeExpensesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(STIncomeExpensesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sTIncomeExpenses on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sTIncomeExpenses).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
