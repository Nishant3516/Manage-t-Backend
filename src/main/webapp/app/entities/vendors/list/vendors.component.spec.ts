import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { VendorsService } from '../service/vendors.service';

import { VendorsComponent } from './vendors.component';

describe('Vendors Management Component', () => {
  let comp: VendorsComponent;
  let fixture: ComponentFixture<VendorsComponent>;
  let service: VendorsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [VendorsComponent],
    })
      .overrideTemplate(VendorsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VendorsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(VendorsService);

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
    expect(comp.vendors?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
