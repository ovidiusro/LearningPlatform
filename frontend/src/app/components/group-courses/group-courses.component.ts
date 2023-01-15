import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { HttpGroupService } from "src/app/services/http-group.service";
import { HttpErrorResponse } from "@angular/common/http";
import { GroupDetailsComponent } from "../group-details/group-details.component";

@Component({
  selector: "app-group-courses",
  templateUrl: "./group-courses.component.html",
  styleUrls: ["./group-courses.component.css"],
})
export class GroupCoursesComponent implements OnInit {
  public isCollapsed = [];
  user: any;
  lessons: any;
  idGroup: number;
  lenOfLessons: number;
  http: string;

  constructor(
    private route: ActivatedRoute,
    private httpGroupService: HttpGroupService,
    private groupDetailsComponent: GroupDetailsComponent
  ) {}

  ngOnInit() {
    this.idGroup =
      +this.groupDetailsComponent.route.snapshot.paramMap.get("id");

    this.groupDetailsComponent.groupSubjectCast.subscribe((group: any) => {
      this.user = group.owner;
      if (this.user.avatar !== undefined) {
        this.http = "http://localhost:8080/api/files/" + this.user.avatar.id;
      }
    });

    this.httpGroupService.getAllLesson(this.idGroup).subscribe(
      (lessons: any) => {
        this.lessons = lessons;
        this.lenOfLessons = this.lessons.length;
        for (let i = 1; i <= this.lenOfLessons; i++) {
          this.isCollapsed[i] = true;
        }
      },
      (err: HttpErrorResponse) => {
        console.log(err);
      }
    );
  }
}
