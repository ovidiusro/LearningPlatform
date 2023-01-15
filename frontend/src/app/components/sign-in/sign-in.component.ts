import { Component, OnInit } from "@angular/core";
import { HttpAccountService } from "../../services/httpAccount.service";
import { Router } from "@angular/router";
import { HttpErrorResponse } from "@angular/common/http";
import { AppComponent } from "../../app.component";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

@Component({
  selector: "app-sign-in",
  templateUrl: "./sign-in.component.html",
  styleUrls: ["./sign-in.component.css"],
})
export class SignInComponent implements OnInit {
  errorMsg: String;
  credentials = { username: "", password: "" };
  signInForm: FormGroup;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private httpAccountService: HttpAccountService,
    private router: Router,
    private appComponent: AppComponent
  ) {}

  ngOnInit() {
    this.signInForm = this.formBuilder.group({
      username: ["", Validators.required],
      password: ["", Validators.required],
    });
  }
  get f() {
    return this.signInForm.controls;
  }

  sign_in() {
    this.submitted = true;
    if (this.signInForm.invalid) {
      return;
    }
    this.credentials.username = this.f.username.value;
    this.credentials.password = this.f.password.value;

    this.httpAccountService.signIn(this.credentials).subscribe(
      (data: any) => {
        localStorage.setItem("userToken", data.token);
        localStorage.setItem("isLogged", "true");
        this.appComponent.isLogged = true;
        this.errorMsg = "";

        this.httpAccountService.getCurrentUser().subscribe((res: any) => {
          localStorage.setItem("currentUser", JSON.stringify(res.principal));
          this.httpAccountService.setIsLogged(true);
          this.router.navigateByUrl("/home");
        });
      },
      (err: HttpErrorResponse) => {
        localStorage.setItem("isLogged", "false");
        this.httpAccountService.setIsLogged(false);
        this.errorMsg = "Sorry, we don not recognize those credentials.";
      }
    );

    return false;
  }
}
