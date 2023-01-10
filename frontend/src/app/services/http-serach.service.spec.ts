import { TestBed, inject } from '@angular/core/testing';

import { HttpSerachService } from './http-serach.service';

describe('HttpSerachService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpSerachService]
    });
  });

  it('should be created', inject([HttpSerachService], (service: HttpSerachService) => {
    expect(service).toBeTruthy();
  }));
});
