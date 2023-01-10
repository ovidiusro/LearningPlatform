import { Component, OnInit } from '@angular/core';
import { HttpGroupService } from '../../services/http-group.service';
import { HttpSerachService } from 'src/app/services/http-serach.service';
import { HomeComponent } from '../home/home.component';

@Component({
  selector: 'app-home-private',
  templateUrl: './home-private.component.html',
  styleUrls: ['./home-private.component.css']
})
export class HomePrivateComponent implements OnInit {

  groups: any;
  httpSearchService: any;
  httpRoot = 'http://localhost:8080/api/files/';


  constructor( private httpGroupService: HttpGroupService,
               private homeComponent: HomeComponent) {
                 this.httpSearchService = homeComponent.getHttpSearchService();
   }

  ngOnInit() {
    if (this.groups !== null) {
    this.getAll();
    }
    this.httpSearchService.castSearchTermForPrivate.subscribe(searchTerm => {
      if (searchTerm !== '' && searchTerm !== undefined) {
        this.search(searchTerm);
      }
    });
  }

  getAll() {
      this.httpGroupService.getAll().subscribe((res: any) => {
        this.groups = res;
      });
  }

  search(searchTerm) {
    console.log('serach in private');
      this.httpSearchService.search(searchTerm).subscribe((res: any) => {
        this.groups = res;
      });
  }


}
