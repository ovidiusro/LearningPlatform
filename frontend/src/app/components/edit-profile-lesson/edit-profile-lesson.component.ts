import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { HttpGroupService } from 'src/app/services/http-group.service';

import { UploadFileComponent } from '../upload-file/upload-file.component';
import { HttpLessonService } from 'src/app/services/http-lesson.service';
import { EditLessonComponent } from '../edit-lesson/edit-lesson.component';

@Component({
  selector: 'app-edit-profile-lesson',
  templateUrl: './edit-profile-lesson.component.html',
  styleUrls: ['./edit-profile-lesson.component.css']
})
export class EditProfileLessonComponent implements OnInit {

  uploadErrorMsg: String;
  uploadMsg: String;
  errorMsg: String;
  msg: String;
  updateLessonForm: FormGroup;
  submitted = false;
  message: String;
  uploadFileMsg: String;

  videoEventReceived = false;
  idOfMetadataFile: String;

  lesson: any;

  constructor(public route: ActivatedRoute,
    private editLesson: EditLessonComponent,
    private httpLessonService: HttpLessonService,
    private formBuilder: FormBuilder,
    private router: Router) { }


  ngOnInit() {
    this.updateLessonForm = this.formBuilder.group({
      title: ['', [Validators.minLength(3), Validators.maxLength(100)]],
      orderNumber: ['', [Validators.pattern('^[0-9]*$')]],
      shortDescription: ['', [Validators.minLength(3), Validators.maxLength(1000)]],
      body: ['', [Validators.minLength(10), Validators.maxLength(50000)]]
    });

    this.editLesson.lessonSubjectCast.subscribe((lesson: any) => {
      this.lesson = lesson;
    });
  }
  get f() { return this.updateLessonForm.controls; }

  receiveEventForUploadVideo($event)  {
    this.videoEventReceived = true;
    this.idOfMetadataFile = $event;
  }


  uploadVideo() {
    this.uploadErrorMsg = '';
    this.uploadMsg = '';

    if (this.videoEventReceived) {
      this.httpLessonService.setVideo(this.lesson.id, this.idOfMetadataFile).subscribe((res: any) => {
        this.uploadMsg = 'Video registered successfully';
        this.videoEventReceived = false;
      });
    }
  }

  updateLesson() {
    this.errorMsg = '';
    this.msg = '';
    this.submitted = true;

    if (this.updateLessonForm.invalid) {
      return;
    }
    this.httpLessonService.update(this.lesson.id, this.updateLessonForm.value).subscribe((updatedLesson: any) => {
      this.lesson = updatedLesson;
      this.msg = 'Lesson updated successfully';
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.errorMsg = err.error.message;
      });
    }
}
