import { Component, OnInit } from "@angular/core";
import { HttpGroupService } from "../../services/http-group.service";
import { ActivatedRoute } from "@angular/router";
import { BehaviorSubject } from "rxjs";

@Component({
  selector: "app-group-details",
  templateUrl: "./group-details.component.html",
  styleUrls: ["./group-details.component.css"],
})
export class GroupDetailsComponent implements OnInit {
  constructor(
    public route: ActivatedRoute,
    private httpGroupService: HttpGroupService
  ) {}

  group: any;
  groupSubject = new BehaviorSubject<any>(false);
  groupSubjectCast = this.groupSubject.asObservable();

  ngOnInit() {
    this.getGroup();
  }

  getGroup(): void {
    const id = +this.route.snapshot.paramMap.get("id");
    this.httpGroupService.getGroupById(id).subscribe((group) => {
      this.group = group;
      this.groupSubject.next(group);
    });
  }
}
