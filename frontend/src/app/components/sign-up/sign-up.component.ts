import { Component, OnInit } from '@angular/core';
import { HttpAccountService } from '../../services/httpAccount.service';
import { Router } from '@angular/router';
import { AppComponent } from '../../app.component';
import { HttpErrorResponse } from '@angular/common/http';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { ChangeDetectorRef } from '@angular/core';


@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {

  errorMsg: String;
  msg: String;
  signUpForm: FormGroup;
  submitted = false;

  constructor(private cd: ChangeDetectorRef, private formBuilder: FormBuilder,
    private httpAccountService: HttpAccountService, private router: Router, private appComponent: AppComponent) {
  }

  ngOnInit() {

    this.signUpForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(6)]],
    });
  }
  get f() { return this.signUpForm.controls; }

  signUp() {
    this.errorMsg = '';
    this.msg = '';
    this.submitted = true;

    if (this.signUpForm.invalid) {
      return;
    }
    if (!this.f.password.value.match(this.f.confirmPassword.value)) {
      this.errorMsg = 'Passwords do not match !';
      return;
    }

    this.httpAccountService.getUserByUsername(this.f.username.value).subscribe((response: any) => {
      this.errorMsg = 'Username is already taken!';
    }, (err) => {
      if (err.status === 404) {
        this.httpAccountService.getUserByEmail(this.f.email.value).subscribe((response2: any) => {
          this.errorMsg = 'An account with this email already exists !';
        }, (err2) => {
          if (err2.status === 404) {
            this.httpAccountService.signUp(this.signUpForm.value).subscribe((data: any) => {
              this.msg = 'Your account has been successfuly created. Please sign in.';
              this.router.navigateByUrl('/sign-up-success');
            },
              (err3: HttpErrorResponse) => {
                console.log(err.message);
                this.errorMsg = err.message;
                this.errorMsg = 'Sorry, an error occured when creating your accont.';
                this.msg = '';
              });
          }
        });

      }

    });

    return false;
  }

}
