import { Injectable } from "@angular/core";
import {
  HttpHeaders,
  HttpClient,
} from "../../../node_modules/@angular/common/http";
import { Router } from "../../../node_modules/@angular/router";

@Injectable({
  providedIn: "root",
})
export class HttpGroupService {
  rootUrl: String = "http://localhost:8080/api/group";
  noAuthReqHeader = new HttpHeaders({ "No-Auth": "True" });

  constructor(private http: HttpClient, private router: Router) {}

  getAll() {
    return this.http.get(this.rootUrl + "/all", {
      headers: this.noAuthReqHeader,
    });
  }
  getGroupById(id) {
    return this.http.get(this.rootUrl + "/" + id, {
      headers: this.noAuthReqHeader,
    });
  }
  deleteGroup(idGroup) {
    return this.http.delete(this.rootUrl + "/" + idGroup);
  }
  getFolowers(id) {
    return this.http.get(this.rootUrl + "/" + id + "/followers", {
      headers: this.noAuthReqHeader,
    });
  }
  getOwnerByID(id) {
    return this.http.get(this.rootUrl + "/" + id + "/owner", {
      headers: this.noAuthReqHeader,
    });
  }
  createGroup(groupForm) {
    return this.http.post(this.rootUrl + "", groupForm);
  }
  setBanner(idGroup, idFile) {
    return this.http.post(
      this.rootUrl + "/banner?groupId=" + idGroup + "&fileId=" + idFile,
      ""
    );
  }
  setCover(idGroup, idFile) {
    return this.http.post(
      this.rootUrl + "/cover?groupId=" + idGroup + "&fileId=" + idFile,
      ""
    );
  }
  setOwner(idGroup, idPrincipal) {
    return this.http.post(
      this.rootUrl + "/" + idGroup + "/owner/" + idPrincipal,
      idGroup
    );
  }
  addFollower(idGroup, idPrincipal) {
    return this.http.put(
      this.rootUrl + "/" + idGroup + "/follower/" + idPrincipal,
      idGroup
    );
  }
  removeFollower(idGroup, idPrincipal) {
    return this.http.delete(
      this.rootUrl + "/" + idGroup + "/follower/" + idPrincipal,
      idGroup
    );
  }

  getAllLesson(idGroup) {
    return this.http.get(this.rootUrl + "/" + idGroup + "/lesson/all");
  }
  getLessonByID(idGroup, idLesson) {
    return this.http.get(this.rootUrl + "/" + idGroup + "/lesson/" + idLesson);
  }
  addLesson(idLesson, idFile) {
    return this.http.post(
      this.rootUrl + "/" + idLesson + "/lesson/" + idFile,
      idFile
    );
  }
  removeLesson(idLesson, idFile) {
    return this.http.delete(
      this.rootUrl + "/" + idLesson + "/lesson/" + idFile
    );
  }
  update(idGroup, updatedGroupForm) {
    return this.http.post(this.rootUrl + "/" + idGroup, updatedGroupForm);
  }
}
