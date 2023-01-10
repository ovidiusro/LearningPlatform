import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { HttpGroupService } from 'src/app/services/http-group.service';

import { EditGroupComponent } from '../edit-group/edit-group.component';

@Component({
  selector: 'app-manage-lessons',
  templateUrl: './manage-lessons.component.html',
  styleUrls: ['./manage-lessons.component.css']
})
export class ManageLessonsComponent implements OnInit {

  constructor(public route: ActivatedRoute,
    private navRoute: Router,
    private editGroup: EditGroupComponent,
    private httpGroupService: HttpGroupService,
    private router: Router) { }

  group: any;
  lessons: any;

  ngOnInit() {
    this.editGroup.groupSubjectCast.subscribe((group: any) => {
      if (group.id) {
        this.group = group;
        this.getAllLessons();
      }
    });
}

getAllLessons() {
  this.httpGroupService.getAllLesson(this.group.id).subscribe((lessons: any) => {
    this.lessons = lessons;
  });
}

deleteLesson(idLesson) {
  console.log(idLesson);
  this.httpGroupService.removeLesson(this.group.id, idLesson).subscribe((res: any) => {
    console.log(res);
    this.getAllLessons();
  });
}

navToOverview(idLesson) {
  this.navRoute.navigateByUrl('lesson/' + idLesson);
}
navToEdit(idLesson) {
  this.navRoute.navigateByUrl('/edit-lesson/' + idLesson + '/update');
}


navToCreateLesson() {
  this.router.navigateByUrl('create-lesson/' + this.group.id);
}




}
