import { Component, OnInit } from "@angular/core";
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormControl,
} from "@angular/forms";
import { HttpAccountService } from "../../services/httpAccount.service";
import { Router } from "@angular/router";
import { HttpErrorResponse } from "@angular/common/http";
import { AppComponent } from "src/app/app.component";

@Component({
  selector: "app-profile",
  templateUrl: "./profile.component.html",
  styleUrls: ["./profile.component.css"],
})
export class ProfileComponent implements OnInit {
  updateProfileForm: FormGroup;
  user: any;
  http: string;
  errorMsg: String;
  msg: String;

  avatarEventReceived = false;
  idMetadataAvatar: String;

  constructor(
    private formBuilder: FormBuilder,
    private httpAccountService: HttpAccountService,
    private router: Router,
    private appComponent: AppComponent
  ) {}

  ngOnInit() {
    this.user = JSON.parse(localStorage.getItem("currentUser"));
    if (this.user.avatar !== null) {
      this.http = "http://localhost:8080/api/files/" + this.user.avatar.id;
    }
    this.updateProfileForm = this.formBuilder.group({
      name: ["", [Validators.minLength(3), Validators.maxLength(50)]],
      username: ["", [Validators.maxLength(50), Validators.minLength(3)]],
      password: ["", [Validators.maxLength(50), Validators.minLength(6)]],
      confirmPassword: [
        "",
        [Validators.maxLength(50), Validators.minLength(6)],
      ],
      email: [
        "",
        [Validators.maxLength(50), Validators.minLength(3), Validators.email],
      ],
      profession: ["", [Validators.maxLength(50), Validators.minLength(3)]],
      description: ["", [Validators.maxLength(1000), Validators.minLength(3)]],
      age: ["", [Validators.pattern("^[0-9]*$")]],
      teachAt: ["", [Validators.maxLength(50), Validators.minLength(3)]],
      studyAt: ["", [Validators.maxLength(50), Validators.minLength(3)]],
    });
  }
  get f() {
    return this.updateProfileForm.controls;
  }

  receiveEventForUploadAvatar($event) {
    this.avatarEventReceived = true;
    this.idMetadataAvatar = $event;
  }

  registerFile() {
    this.errorMsg = "";
    this.msg = "";

    if (this.avatarEventReceived) {
      this.httpAccountService
        .setAvatar(this.user.id, this.idMetadataAvatar)
        .subscribe(
          (res: any) => {
            this.avatarEventReceived = false;
            this.msg = "Your profile image has been updated";
            this.appComponent.signOut();
            this.router.navigateByUrl("/sign-up-success");
          },
          (err: HttpErrorResponse) => {}
        );
    }
  }

  updateProfile() {
    this.errorMsg = "";
    this.msg = "";
    let emailUpdate: Boolean = false;
    let usernameUpdate: Boolean = false;

    if (this.updateProfileForm.invalid) {
      return;
    }
    if (!this.f.password.value.match(this.f.confirmPassword.value)) {
      this.errorMsg = "Passwords do not match !";
      return;
    }

    if (this.f.username.value !== "") {
      usernameUpdate = true;
    }

    if (this.f.email.value !== "") {
      emailUpdate = true;
    }

    if (usernameUpdate && emailUpdate) {
      this.httpAccountService
        .getUserByUsername(this.f.username.value)
        .subscribe(
          (response: any) => {
            this.errorMsg = "Username is already taken!";
          },
          (err) => {
            if (err.status === 404) {
              this.httpAccountService
                .getUserByEmail(this.f.email.value)
                .subscribe(
                  (response: any) => {
                    this.errorMsg = "Email is already taken!";
                    return;
                  },
                  (err2) => {
                    if (err2.status === 404) {
                      this.updateAccount();
                    }
                  }
                );
            }
          }
        );
    } else if (usernameUpdate || emailUpdate) {
      if (usernameUpdate) {
        this.httpAccountService
          .getUserByUsername(this.f.username.value)
          .subscribe(
            (response: any) => {
              this.errorMsg = "Username is already taken!";
            },
            (err) => {
              if (err.status === 404) {
                this.updateAccount();
              }
            }
          );
      }

      if (emailUpdate) {
        this.httpAccountService.getUserByEmail(this.f.email.value).subscribe(
          (response: any) => {
            this.errorMsg = "Email is already taken!";
          },
          (err) => {
            if (err.status === 404) {
              this.updateAccount();
            }
          }
        );
      }
    } else {
      this.updateAccount();
    }
  }

  updateAccount() {
    this.httpAccountService
      .update(this.user.id, this.updateProfileForm.value)
      .subscribe(
        (res: any) => {
          this.msg = "Your accout has been updated";
          this.appComponent.signOut();
          this.router.navigateByUrl("/sign-up-success");
        },
        (err3) => {
          console.log(err3.message);
          this.errorMsg = err3.message;
          this.errorMsg = "Sorry, an error occured when updating your account.";
          this.msg = "";
          return false;
        }
      );
  }
}
