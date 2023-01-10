import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { HttpAccountService } from './services/httpAccount.service';
import { HomePrivateComponent } from './components/home-private/home-private.component';
import { HomePublicComponent } from './components/home-public/home-public.component';
import { HttpSerachService } from './services/http-serach.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  isLogged = false;

  constructor(
     private http: HttpClient,
     private router: Router,
     private httpAccount: HttpAccountService,
     private httpSearchService: HttpSerachService) { }


  ngOnInit() {
    this.initVariables();
    this.httpAccount.castLogInfo.subscribe(isLogged => {
      this.isLogged = isLogged;
    });
  }


  initVariables() {
    if (localStorage.getItem('userToken') != null) {
      return this.httpAccount.checkToken().subscribe(
        res => {
          localStorage.setItem('isLogged', 'true');
          this.httpAccount.setIsLogged(JSON.parse(localStorage.getItem('isLogged')));
        },
        err => {
          console.log('checktoken fails' + err);
          localStorage.setItem('isLogged', 'false');
          localStorage.removeItem('userToken');
          localStorage.removeItem('currentUser');
          this.httpAccount.setIsLogged(JSON.parse(localStorage.getItem('isLogged')));
        }
      );
    }
  }

  search(searchTerm: string) {
    if (searchTerm !== '' && searchTerm !== undefined) {
      if (this.isLogged) {
        console.log('app component ' + this.isLogged);
        this.httpSearchService.setSearchTermForPrivate(searchTerm);
      } else {
        console.log('app component ' + this.isLogged);
        this.httpSearchService.setSearchTermForPublic(searchTerm);
      }

    }
    this.router.navigateByUrl('/home');
  }

  signOut() {
    this.httpAccount.signOut().subscribe(
      res => {
        localStorage.setItem('isLogged', 'false');
        localStorage.removeItem('userToken');
        localStorage.removeItem('currentUser');
        this.isLogged = false;
        this.httpAccount.setIsLogged(false);
        this.router.navigateByUrl('/sign-in');
      },
      err => {
        console.log('sign-out fails' + err);
      });
  }
}
