import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { IdStoreService } from '../service/id-store.service';

import { IdStoreComponent } from './id-store.component';

describe('Component Tests', () => {
  describe('IdStore Management Component', () => {
    let comp: IdStoreComponent;
    let fixture: ComponentFixture<IdStoreComponent>;
    let service: IdStoreService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IdStoreComponent],
      })
        .overrideTemplate(IdStoreComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IdStoreComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(IdStoreService);

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
      expect(comp.idStores?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
