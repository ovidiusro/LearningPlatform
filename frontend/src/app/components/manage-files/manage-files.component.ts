import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Router } from "@angular/router";
import { EditLessonComponent } from "../edit-lesson/edit-lesson.component";
import { HttpLessonService } from "src/app/services/http-lesson.service";
import { HttpFilesService } from "src/app/services/http-files.service";
import { HttpErrorResponse } from "@angular/common/http";

@Component({
  selector: "app-manage-files",
  templateUrl: "./manage-files.component.html",
  styleUrls: ["./manage-files.component.css"],
})
export class ManageFilesComponent implements OnInit {
  idLesson: any;
  lesson: any;

  files: any;
  filesEventReceived = false;
  idOfMetadataFile: String;

  uploadErrorMsg: String;
  uploadMsg: String;

  constructor(
    public route: ActivatedRoute,
    private navRoute: Router,
    private editLesson: EditLessonComponent,
    private httpFileService: HttpFilesService,
    private httpLessonService: HttpLessonService,
    private router: Router
  ) {}

  ngOnInit() {
    this.idLesson = +this.editLesson.route.snapshot.paramMap.get("id");
    this.editLesson.lessonSubjectCast.subscribe((lesson: any) => {
      this.lesson = lesson;
      if (lesson) {
        this.getAllFiles();
      }
    });
  }

  receiveEventForUploadFile($event) {
    this.filesEventReceived = true;
    this.idOfMetadataFile = $event;
  }

  registerFile() {
    this.uploadErrorMsg = "";
    this.uploadMsg = "";

    if (this.filesEventReceived) {
      this.httpLessonService
        .addFile(this.lesson.id, this.idOfMetadataFile)
        .subscribe(
          (res: any) => {
            this.filesEventReceived = false;
            this.uploadMsg = "File registered successfully ! ";
            this.getAllFiles();
          },
          (err: HttpErrorResponse) => {
            this.uploadErrorMsg = "Something went wrong, please try again ! ";
          }
        );
    }
  }

  getAllFiles() {
    this.httpLessonService
      .getAllFiles(this.lesson.id)
      .subscribe((files: any) => {
        this.files = files;
      });
  }

  removeFile(idFile) {
    this.httpLessonService
      .removeFile(this.idLesson, idFile)
      .subscribe((res: any) => {
        console.log(res);
        this.getAllFiles();
      });
  }

  downloadFile(idFile) {
    return this.httpFileService.getFileById(idFile).subscribe((response) => {
      this.httpFileService
        .getMetadataById(idFile)
        .subscribe((metaData: any) => {
          this.download(response, metaData.name);
        });
    });
  }
  download(data, fileName) {
    const a = document.createElement("a");
    document.body.appendChild(a);
    const blob = new Blob([data], { type: "octet/stream" }),
      url = window.URL.createObjectURL(blob);
    a.href = url;
    a.download = fileName;
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
