import { Component, OnInit, AfterViewInit } from "@angular/core";
import { Router } from "@angular/router";
import { HttpErrorResponse } from "@angular/common/http";
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormControl,
} from "@angular/forms";
import { HttpGroupService } from "src/app/services/http-group.service";

import { UploadFileComponent } from "../upload-file/upload-file.component";

@Component({
  selector: "app-create-group",
  templateUrl: "./create-group.component.html",
  styleUrls: ["./create-group.component.css"],
})
export class CreateGroupComponent implements OnInit {
  errorMsg: String;
  msg: String;
  groupForm: FormGroup;
  submitted = false;
  message: String;
  eventReceived = false;
  idOfMetadataCover: String;
  idOfMetadataBanner: String;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private httpGroupService: HttpGroupService
  ) {}

  ngOnInit() {
    this.groupForm = this.formBuilder.group({
      name: [
        "",
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(50),
        ],
      ],
      shortDescription: [
        "",
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(244),
        ],
      ],
      longDescription: [
        "",
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(15000),
        ],
      ],
    });
  }

  receiveEventForBanner($event) {
    this.eventReceived = true;
    this.idOfMetadataBanner = $event;
  }
  receiveEventForCover($event) {
    this.eventReceived = true;
    this.idOfMetadataCover = $event;
  }

  get f() {
    return this.groupForm.controls;
  }

  createGroup() {
    this.errorMsg = "";
    this.msg = "";
    this.submitted = true;

    if (this.groupForm.invalid) {
      return;
    }

    this.httpGroupService.createGroup(this.groupForm.value).subscribe(
      (newGroup: any) => {
        this.msg = "Group has been successfully created";
        if (this.eventReceived) {
          this.httpGroupService
            .setBanner(newGroup.id, this.idOfMetadataBanner)
            .subscribe((res2: any) => {
              console.log("response after trying to set the banner" + res2);
              this.httpGroupService
                .setCover(newGroup.id, this.idOfMetadataCover)
                .subscribe((res3: any) => {
                  console.log("response after trying to set the cover" + res2);
                });
            });
        }
        const principal = JSON.parse(localStorage.getItem("currentUser"));
        this.httpGroupService.setOwner(newGroup.id, principal.id).subscribe(
          (response: any) => {},
          (err: HttpErrorResponse) => {
            console.log("set owner " + err);
          }
        );
      },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.errorMsg = err.error.message;
      }
    );
  }
}
