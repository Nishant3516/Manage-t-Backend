import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SchoolEventService } from '../service/school-event.service';

import { SchoolEventComponent } from './school-event.component';

describe('Component Tests', () => {
  describe('SchoolEvent Management Component', () => {
    let comp: SchoolEventComponent;
    let fixture: ComponentFixture<SchoolEventComponent>;
    let service: SchoolEventService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SchoolEventComponent],
      })
        .overrideTemplate(SchoolEventComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SchoolEventComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SchoolEventService);

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
      expect(comp.schoolEvents?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
