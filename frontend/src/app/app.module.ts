import { BrowserModule } from '@angular/platform-browser';
import { NgModule, ViewContainerRef } from '@angular/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';

import {MatButtonModule} from '@angular/material/button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { SignInComponent } from './components/sign-in/sign-in.component';
import { SignUpSuccessComponent } from './components/sign-up-success/sign-up-success.component';
import { HomePrivateComponent } from './components/home-private/home-private.component';
import { AppRoutingModule } from './app-routing/app-routing.module';
import { HttpAccountService } from './services/httpAccount.service';
import { AuthGuard } from './auth/auth.guard';
import { AuthInterceptor } from './auth/auth.interceptor';
import { HomePublicComponent } from './components/home-public/home-public.component';
import { HttpGroupService } from './services/http-group.service';
import { GroupDetailsComponent } from './components/group-details/group-details.component';
import { GroupOverviewComponent } from './components/group-overview/group-overview.component';
import { GroupCoursesComponent } from './components/group-courses/group-courses.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { FileSelectDirective } from 'ng2-file-upload';


import { from } from 'rxjs';
import { ProfileComponent } from './components/profile/profile.component';
import { CreateGroupComponent } from './components/create-group/create-group.component';
import { UploadFileComponent } from './components/upload-file/upload-file.component';
import { UploadFileDirective} from './upload-file.directive';
import { OtherComponent } from './components/other/other.component';
import { FollowComponent } from './components/follow/follow.component';
import { MyGroupsComponent } from './components/my-groups/my-groups.component';
import { CreateLessonComponent } from './components/create-lesson/create-lesson.component';
import { LessonComponent } from './components/lesson/lesson.component';
import { EditGroupComponent } from './components/edit-group/edit-group.component';
import { EditLessonComponent } from './components/edit-lesson/edit-lesson.component';
import { EditGroupProfileComponent } from './components/edit-group-profile/edit-group-profile.component';
import { ManageLessonsComponent } from './components/manage-lessons/manage-lessons.component';
import { EditProfileLessonComponent } from './components/edit-profile-lesson/edit-profile-lesson.component';
import { ManageFilesComponent } from './components/manage-files/manage-files.component';


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    PageNotFoundComponent,
    SignInComponent,
    SignUpComponent,
    HomePublicComponent,
    HomePrivateComponent,
    SignUpSuccessComponent,
    GroupDetailsComponent,
    GroupOverviewComponent,
    GroupCoursesComponent,
    ProfileComponent,
    FileSelectDirective,
    CreateGroupComponent,
    UploadFileComponent,
    UploadFileDirective,
    OtherComponent,
    FollowComponent,
    MyGroupsComponent,
    CreateLessonComponent,
    LessonComponent,
    EditGroupComponent,
    EditLessonComponent,
    EditGroupProfileComponent,
    ManageLessonsComponent,
    EditProfileLessonComponent,
    ManageFilesComponent,

  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModule,
  ],
  providers: [HttpAccountService, HttpGroupService, AuthGuard,
  {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true
  }
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
