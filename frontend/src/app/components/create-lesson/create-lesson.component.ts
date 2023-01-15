import { ActivatedRoute } from "@angular/router";
import { Component, OnInit, AfterViewInit } from "@angular/core";
import { HttpErrorResponse } from "@angular/common/http";
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormControl,
} from "@angular/forms";
import { HttpGroupService } from "src/app/services/http-group.service";
import { UploadFileComponent } from "../upload-file/upload-file.component";
import { HttpLessonService } from "src/app/services/http-lesson.service";

@Component({
  selector: "app-create-lesson",
  templateUrl: "./create-lesson.component.html",
  styleUrls: ["./create-lesson.component.css"],
})
export class CreateLessonComponent implements OnInit {
  idGroup: number;
  errorMsg: String;
  msg: String;
  lessonForm: FormGroup;
  submitted = false;

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private httpLesssonService: HttpLessonService,
    private httpGroupService: HttpGroupService
  ) {}

  get f() {
    return this.lessonForm.controls;
  }

  ngOnInit() {
    this.idGroup = +this.route.snapshot.paramMap.get("id");
    this.lessonForm = this.formBuilder.group({
      title: [
        "",
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(100),
        ],
      ],
      orderNumber: ["", [Validators.required]],
      shortDescription: [
        "",
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(1000),
        ],
      ],
      body: [
        "",
        [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(50000),
        ],
      ],
    });
  }

  registerLesson() {
    this.errorMsg = "";
    this.msg = "";
    this.submitted = true;

    if (this.lessonForm.invalid) {
      return;
    }

    this.httpLesssonService.registerLesson(this.lessonForm.value).subscribe(
      (newLesson: any) => {
        this.httpGroupService
          .addLesson(this.idGroup, newLesson.id)
          .subscribe((res: any) => {});
        this.msg = "Lesson has been successfully created";
        console.log(newLesson);
      },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.errorMsg = err.error.message;
      }
    );
  }
}
