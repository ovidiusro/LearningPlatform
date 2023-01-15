import { Injectable } from "@angular/core";
import {
  HttpHeaders,
  HttpClient,
} from "../../../node_modules/@angular/common/http";
import { Router } from "../../../node_modules/@angular/router";

@Injectable({
  providedIn: "root",
})
export class HttpLessonService {
  rootUrl: String = "http://localhost:8080/api/lesson";
  noAuthReqHeader = new HttpHeaders({ "No-Auth": "True" });

  constructor(private http: HttpClient, private router: Router) {}

  getAll() {
    return this.http.get(this.rootUrl + "/all");
  }
  getById(id) {
    return this.http.get(this.rootUrl + "/" + id);
  }
  registerLesson(lessonForm) {
    console.log(lessonForm);
    return this.http.post(this.rootUrl + "", lessonForm);
  }
  delete(idLesson) {
    return this.http.delete(this.rootUrl + "/" + idLesson);
  }
  update(idLesson, lessonForm) {
    return this.http.post(this.rootUrl + "/" + idLesson, lessonForm);
  }

  getFileById(idLesson, idFile) {
    return this.http.get(this.rootUrl + "/" + idLesson + "/file/" + idFile);
  }
  addFile(idLesson, idFile) {
    return this.http.post(
      this.rootUrl + "/" + idLesson + "/file/" + idFile,
      idLesson
    );
  }
  removeFile(idLesson, idFile) {
    return this.http.delete(this.rootUrl + "/" + idLesson + "/file/" + idFile);
  }
  getAllFiles(idLesson) {
    return this.http.get(this.rootUrl + "/" + idLesson + "/files");
  }
  removeFileFromLessonById(idLesson, idFile) {
    return this.http.delete(this.rootUrl + "/" + idLesson + "/file/" + idFile);
  }

  getVideo(idLesson) {
    return this.http.get(this.rootUrl + "/" + idLesson + "/video");
  }
  setVideo(idLesson, idFile) {
    return this.http.post(
      this.rootUrl + "/" + idLesson + "/video/" + idFile,
      idLesson
    );
  }
}
