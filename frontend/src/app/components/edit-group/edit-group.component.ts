import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpGroupService } from 'src/app/services/http-group.service';
import { BehaviorSubject } from 'rxjs';


@Component({
  selector: 'app-edit-group',
  templateUrl: './edit-group.component.html',
  styleUrls: ['./edit-group.component.css']
})
export class EditGroupComponent implements OnInit {

  group: any;
  groupSubject = new BehaviorSubject<any>(false);
  groupSubjectCast = this.groupSubject.asObservable();

  constructor(public route: ActivatedRoute,
    private httpGroupService: HttpGroupService,
    private router: Router) { }


  ngOnInit() {
    this.getGroup();
  }
  getGroup(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.httpGroupService.getGroupById(id)
      .subscribe((group) => {
      this.group = group;
      this.groupSubject.next(group);
      });
  }
}
