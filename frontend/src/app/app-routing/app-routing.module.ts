import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import { PageNotFoundComponent } from '../components/page-not-found/page-not-found.component';
import { HomeComponent } from '../components/home/home.component';
import { SignInComponent } from '../components/sign-in/sign-in.component';
import { SignUpComponent } from '../components/sign-up/sign-up.component';
import { AuthGuard } from '../auth/auth.guard';
import { HomePublicComponent } from '../components/home-public/home-public.component';
import { HomePrivateComponent } from '../components/home-private/home-private.component';
import { SignUpSuccessComponent } from '../components/sign-up-success/sign-up-success.component';
import { GroupDetailsComponent } from '../components/group-details/group-details.component';
import { GroupOverviewComponent } from '../components/group-overview/group-overview.component';
import { GroupCoursesComponent } from '../components/group-courses/group-courses.component';
import { ProfileComponent } from '../components/profile/profile.component';
import { CreateGroupComponent } from '../components/create-group/create-group.component';
import { UploadFileComponent } from '../components/upload-file/upload-file.component';
import { OtherComponent } from '../components/other/other.component';
import { FollowComponent } from '../components/follow/follow.component';
import { MyGroupsComponent } from '../components/my-groups/my-groups.component';
import { CreateLessonComponent } from '../components/create-lesson/create-lesson.component';
import { LessonComponent } from '../components/lesson/lesson.component';
import { EditGroupComponent } from '../components/edit-group/edit-group.component';
import { EditGroupProfileComponent } from '../components/edit-group-profile/edit-group-profile.component';
import { ManageLessonsComponent } from '../components/manage-lessons/manage-lessons.component';
import { EditLessonComponent } from '../components/edit-lesson/edit-lesson.component';
import { EditProfileLessonComponent } from '../components/edit-profile-lesson/edit-profile-lesson.component';
import { ManageFilesComponent } from '../components/manage-files/manage-files.component';


const routes: Routes = [
{path: '', redirectTo: 'home', pathMatch: 'full'},
{
  path: 'home',
  component: HomeComponent,
  children: [
    {path: 'home-public', component: HomePublicComponent},
    {path: 'home-private', component: HomePrivateComponent, canActivate: [AuthGuard]},
  ]
},

{path: 'edit-lesson/:id', component: EditLessonComponent, canActivate: [AuthGuard],
  children: [
    {path: 'lesson/:id', component: LessonComponent},
    {path: 'update', component: EditProfileLessonComponent},
    {path: 'files', component: ManageFilesComponent},
]},

{path: 'home/group-details/:id', component: GroupDetailsComponent,
children: [
    {path: 'overview', component: GroupOverviewComponent},
    {path: 'courses', component: GroupCoursesComponent},
]},

{path: 'edit-group/:id', component: EditGroupComponent, canActivate: [AuthGuard],
  children: [
    {path: 'group-details/:id', component: GroupDetailsComponent,
        children: [
          {path: 'overview', component: GroupOverviewComponent},
          {path: 'courses', component: GroupCoursesComponent},
    ]},
    {path: 'update', component: EditGroupProfileComponent},
    {path: 'lessons', component: ManageLessonsComponent},
]},

{path: 'home/other', component: OtherComponent, canActivate: [AuthGuard],
 children: [
    {path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
    {path: 'follow', component: FollowComponent , canActivate: [AuthGuard]},
    {path: 'myGroups', component: MyGroupsComponent, canActivate: [AuthGuard]}
 ]
},
{path: 'lesson/:id', component: LessonComponent, canActivate: [AuthGuard]},
{path: 'upload-file', component: UploadFileComponent, canActivate: [AuthGuard]},
{path: 'create-group', component: CreateGroupComponent, canActivate: [AuthGuard]},
// {path: 'edit-group/:id', component: EditGroupComponent, canActivate: [AuthGuard]},
{path: 'create-lesson/:id', component: CreateLessonComponent, canActivate: [AuthGuard]},
{path: 'sign-in', component: SignInComponent},
{path: 'sign-up', component: SignUpComponent},
{path: 'sign-up-success', component: SignUpSuccessComponent},
{path: '**', component: PageNotFoundComponent},
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
