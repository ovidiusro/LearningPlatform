import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class HttpAccountService {



  rootUrl: String = 'http://localhost:8080/api/account';
  noAuthReqHeader = new HttpHeaders({ 'No-Auth': 'True' });

  private isLogged = new BehaviorSubject<boolean>(false);

  castLogInfo = this.isLogged.asObservable();

  constructor(private http: HttpClient) { }


  setIsLogged(isLogged) {
    this.isLogged.next(isLogged);
  }

  signIn(credentials) {
    return this.http.post(this.rootUrl + '/sign-in', credentials, { headers: this.noAuthReqHeader });
  }
  signUp(registerForm) {
    return this.http.post(this.rootUrl + '/sign-up', registerForm, { headers: this.noAuthReqHeader });
  }
  update(idUser, updateForm) {
    return this.http.post(this.rootUrl + '/' + idUser , updateForm);
  }

  getCurrentUser() {
    return this.http.get(this.rootUrl + '/current');
  }

  signOut() {
    return this.http.get(this.rootUrl + '/sign-out');
  }

  checkToken() {
    return this.http.get(this.rootUrl + '/check-token');
  }

  getUserByUsername(username) {
    return this.http.get(this.rootUrl + '/username/' + username, { headers: this.noAuthReqHeader });
  }

  setAvatar(idAccount, idFile) {
    return this.http.post(this.rootUrl + '/avatar?accountId=' + idAccount + '&fileId=' + idFile, idAccount);
 }
  getAvatar(idAccount) {
    return this.http.get(this.rootUrl + '/avatar/' + idAccount);
 }

  getUserByEmail(email) {
    return this.http.get(this.rootUrl + '/email/' + email, { headers: this.noAuthReqHeader });
  }

  getFollowGroups(idAccount) {
    return this.http.get(this.rootUrl + '/' + idAccount + '/followGroups');
  }
  getOwnGroups(idAccount) {
    return this.http.get(this.rootUrl + '/' + idAccount + '/ownGroups');
  }
}
