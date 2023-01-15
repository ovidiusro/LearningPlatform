import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
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
import { EditGroupComponent } from "../edit-group/edit-group.component";

@Component({
  selector: "app-edit-group-profile",
  templateUrl: "./edit-group-profile.component.html",
  styleUrls: ["./edit-group-profile.component.css"],
})
export class EditGroupProfileComponent implements OnInit {
  errorMsg: String;
  msg: String;
  updateGroupForm: FormGroup;
  submitted = false;
  message: String;
  uploadFileMsg: String;
  idGroup: number;
  bannerEventReceived = false;
  coverEventReceived = false;
  idOfMetadataCover: String;
  idOfMetadataBanner: String;
  lessons: any;
  group: any;

  constructor(
    public route: ActivatedRoute,
    private editGroup: EditGroupComponent,
    private httpGroupService: HttpGroupService,
    private formBuilder: FormBuilder,
    private router: Router
  ) {}

  ngOnInit() {
    this.updateGroupForm = this.formBuilder.group({
      name: ["", [Validators.minLength(3), Validators.maxLength(50)]],
      shortDescription: [
        "",
        [Validators.minLength(3), Validators.maxLength(244)],
      ],
      longDescription: [
        "",
        [Validators.minLength(10), Validators.maxLength(15000)],
      ],
    });
    this.idGroup = +this.editGroup.route.snapshot.paramMap.get("id");

    this.editGroup.groupSubjectCast.subscribe((group: any) => {
      this.group = group;
    });
  }

  get f() {
    return this.updateGroupForm.controls;
  }

  receiveEventForBanner($event) {
    this.bannerEventReceived = true;
    this.idOfMetadataBanner = $event;
  }
  receiveEventForCover($event) {
    this.coverEventReceived = true;
    this.idOfMetadataCover = $event;
  }

  updateGroup() {
    this.errorMsg = "";
    this.msg = "";
    this.submitted = true;

    console.log(this.updateGroupForm);
    if (this.updateGroupForm.invalid) {
      return;
    }
    this.httpGroupService
      .update(this.group.id, this.updateGroupForm.value)
      .subscribe(
        (updatedGroup: any) => {
          this.msg = "Group updated successfully";
        },
        (err: HttpErrorResponse) => {
          this.errorMsg = err.error.message;
        }
      );
    if (this.coverEventReceived && this.bannerEventReceived) {
      this.httpGroupService
        .setBanner(this.group.id, this.idOfMetadataBanner)
        .subscribe((res2: any) => {
          console.log("response after trying to update the banner" + res2);
          this.bannerEventReceived = false;
          this.httpGroupService
            .setCover(this.group.id, this.idOfMetadataCover)
            .subscribe((res3: any) => {
              console.log("response after trying to update the cover" + res2);
              this.coverEventReceived = false;
            });
        });
    } else if (this.coverEventReceived || this.bannerEventReceived) {
      if (this.coverEventReceived) {
        this.httpGroupService
          .setCover(this.group.id, this.idOfMetadataCover)
          .subscribe((res2: any) => {
            this.coverEventReceived = false;
            console.log("response after trying to update the cover" + res2);
          });
      } else if (this.bannerEventReceived) {
        this.httpGroupService
          .setBanner(this.group.id, this.idOfMetadataBanner)
          .subscribe((res2: any) => {
            this.bannerEventReceived = false;
            console.log("response after trying to update the banner" + res2);
          });
      }
    }
  }
}
