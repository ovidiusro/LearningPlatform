import { TestBed, inject } from '@angular/core/testing';

import { HttpFilesService } from './http-files.service';

describe('HttpFilesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpFilesService]
    });
  });

  it('should be created', inject([HttpFilesService], (service: HttpFilesService) => {
    expect(service).toBeTruthy();
  }));
});
