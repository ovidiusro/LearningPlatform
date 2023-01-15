import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";

@Injectable({
  providedIn: "root",
})
export class HttpFilesService {
  rootUrl: String = "http://localhost:8080/api/files";
  noAuthReqHeader = new HttpHeaders({ "No-Auth": "True" });

  constructor(private http: HttpClient) {}

  createMetadata(name, summary) {
    return this.http.post(
      this.rootUrl + "/metadata?name=" + name + "&summary=" + summary,
      ""
    );
  }

  deleteMetadata(id) {
    return this.http.delete(this.rootUrl + "/metadata/" + id);
  }

  getFileById(id) {
    return this.http.get(this.rootUrl + "/" + id, { responseType: "blob" });
  }

  getMetadataById(id) {
    return this.http.get(this.rootUrl + "/metadata?id=" + id);
  }

  getMetadataByName(name) {
    return this.http.get(this.rootUrl + "/metadata?name=" + name);
  }
}
