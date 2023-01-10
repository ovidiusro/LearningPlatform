import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '../../../node_modules/@angular/common/http';
import { Router } from '../../../node_modules/@angular/router';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpSerachService {

  rootUrl: String = 'http://localhost:8080/api/group/search';
  noAuthReqHeader = new HttpHeaders({'No-Auth': 'True'});


  constructor(private http: HttpClient, private router: Router) { }

  private searchTermForPrivate = new BehaviorSubject<string>('');
  castSearchTermForPrivate = this.searchTermForPrivate.asObservable();

  private searchTermForPublic = new BehaviorSubject<string>('');
  castSearchTermForPublic = this.searchTermForPublic.asObservable();

  setSearchTermForPublic(searchTermForPublic) {
    this.searchTermForPublic.next(searchTermForPublic);
  }
  setSearchTermForPrivate(castSearchTermForPrivate) {
    this.searchTermForPrivate.next(castSearchTermForPrivate);
  }

  search(searchTerm) {
    return this.http.get(this.rootUrl + '?searchTerm=' + searchTerm, { headers: this.noAuthReqHeader });
  }
}


