import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { IncomeExpensesService } from '../service/income-expenses.service';

import { IncomeExpensesComponent } from './income-expenses.component';

describe('IncomeExpenses Management Component', () => {
  let comp: IncomeExpensesComponent;
  let fixture: ComponentFixture<IncomeExpensesComponent>;
  let service: IncomeExpensesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [IncomeExpensesComponent],
    })
      .overrideTemplate(IncomeExpensesComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IncomeExpensesComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(IncomeExpensesService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
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
    expect(comp.incomeExpenses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
