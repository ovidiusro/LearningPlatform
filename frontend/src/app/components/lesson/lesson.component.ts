import { Component, OnInit } from "@angular/core";
import { HttpLessonService } from "src/app/services/http-lesson.service";
import { HttpGroupService } from "../../services/http-group.service";
import { ActivatedRoute } from "@angular/router";
import { HttpErrorResponse } from "@angular/common/http";
import { HttpFilesService } from "src/app/services/http-files.service";

@Component({
  selector: "app-lesson",
  templateUrl: "./lesson.component.html",
  styleUrls: ["./lesson.component.css"],
})
export class LessonComponent implements OnInit {
  lesson: any;
  idLesson: any;

  constructor(
    private httpLessonService: HttpLessonService,
    private httpGroupSerice: HttpGroupService,
    private route: ActivatedRoute,
    private httpFileService: HttpFilesService
  ) {}

  ngOnInit() {
    this.idLesson = +this.route.snapshot.paramMap.get("id");
    console.log(this.idLesson);
    this.httpLessonService.getById(this.idLesson).subscribe((lesson: any) => {
      this.lesson = lesson;
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
