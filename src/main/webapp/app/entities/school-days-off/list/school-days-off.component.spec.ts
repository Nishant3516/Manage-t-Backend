import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SchoolDaysOffService } from '../service/school-days-off.service';

import { SchoolDaysOffComponent } from './school-days-off.component';

describe('Component Tests', () => {
  describe('SchoolDaysOff Management Component', () => {
    let comp: SchoolDaysOffComponent;
    let fixture: ComponentFixture<SchoolDaysOffComponent>;
    let service: SchoolDaysOffService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SchoolDaysOffComponent],
      })
        .overrideTemplate(SchoolDaysOffComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SchoolDaysOffComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SchoolDaysOffService);

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
      expect(comp.schoolDaysOffs?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
