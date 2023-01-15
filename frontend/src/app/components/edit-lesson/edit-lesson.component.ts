import { Component, OnInit } from "@angular/core";
import { HttpErrorResponse } from "@angular/common/http";
import { HttpGroupService } from "src/app/services/http-group.service";
import { BehaviorSubject } from "rxjs";
import { HttpLessonService } from "src/app/services/http-lesson.service";
import { Router } from "@angular/router";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: "app-edit-lesson",
  templateUrl: "./edit-lesson.component.html",
  styleUrls: ["./edit-lesson.component.css"],
})
export class EditLessonComponent implements OnInit {
  lesson: any;
  lessonSubject = new BehaviorSubject<any>(false);
  lessonSubjectCast = this.lessonSubject.asObservable();

  constructor(
    public route: ActivatedRoute,
    private httpGroupService: HttpGroupService,
    private httpLessonService: HttpLessonService,
    private router: Router
  ) {}

  ngOnInit() {
    this.getLesson();
  }

  getLesson(): void {
    const id = +this.route.snapshot.paramMap.get("id");
    this.httpLessonService.getById(id).subscribe((lesson) => {
      this.lesson = lesson;
      this.lessonSubject.next(lesson);
    });
  }
}
