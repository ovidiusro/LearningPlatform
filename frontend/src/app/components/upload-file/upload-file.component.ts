import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FileUploader, FileSelectDirective } from 'ng2-file-upload/ng2-file-upload';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ChangeDetectorRef } from '@angular/core';
import { HttpHeaders } from 'node_modules/@angular/common/http';
import { HttpFilesService } from 'src/app/services/http-files.service';
import { HttpErrorResponse } from '@angular/common/http';


@Component({
  selector: 'app-upload-file',
  templateUrl: './upload-file.component.html',
  styleUrls: ['./upload-file.component.css']
})
export class UploadFileComponent implements  OnInit {

  url = 'http://localhost:8080/api/files';
  errorMsg: String;
  msg: String;
  metadataForm: FormGroup;
  submitted = false;
  idOfMetadata: String;
  metadataCreated: Boolean = false;
  fileUploaded: Boolean = false;

  @Output() eventEmitter = new EventEmitter<String>();

  constructor(private fileService: HttpFilesService, private cd: ChangeDetectorRef, private formBuilder: FormBuilder) {
  }

  public uploader: FileUploader;

  ngOnInit() {
    this.metadataForm = this.formBuilder.group({
      name: ['', [ Validators.minLength(3)]],
      summary: ['', [ Validators.minLength(3)]],
    });
  }

  emitEvent() {
    console.log('emit event  id: ' + this.idOfMetadata);
    this.eventEmitter.emit(this.idOfMetadata + '');
  }

  get f() { return this.metadataForm.controls; }

  back() {
    if (this.metadataCreated && !this.fileUploaded) {
       this.fileService.deleteMetadata(this.idOfMetadata).subscribe((res) => {
       this.metadataCreated = false;
       this.fileUploaded = false;
       },
       (err: HttpErrorResponse) => {
          console.log(err);
       });
    }
    if (this.fileUploaded && this.metadataCreated) {
       this.metadataCreated = false;
       this.fileUploaded = false;
    }
  }

  createFileMetadata() {
    this.errorMsg = '';
    this.msg = '';
    this.submitted = true;

    if (this.metadataForm.invalid) {
      return;
    }
    this.fileService.createMetadata(this.f.name.value, this.f.summary.value).subscribe((res: any) => {
      console.log(res);
      this.idOfMetadata = res.id;
      this.emitEvent();
      this.msg = 'Parameters for file has been saved';
      this.metadataCreated = true;
      this.uploader = new FileUploader({ url: this.url + '/' + this.idOfMetadata, itemAlias: 'file'});
      this.uploader.onBeforeUploadItem = function (item) {
        item.headers = {
          'Authorization': 'Bearer ' + localStorage.getItem('userToken')
        };
      };
      this.uploader.onAfterAddingFile = (file) => { file.withCredentials = false; };
      this.uploader.onCompleteItem = (item: any, response: any, status: any, headers: any) => {
        this.fileUploaded = true;
      };
    },
      (err: HttpErrorResponse) => {
        this.errorMsg = 'Sorry, an error has occured .';
      });
  }
}
