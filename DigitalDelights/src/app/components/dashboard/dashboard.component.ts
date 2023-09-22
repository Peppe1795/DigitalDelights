import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  selectedSection: string = '';

  constructor() {}

  ngOnInit(): void {}

  navigateTo(section: string): void {
    this.selectedSection = section;
  }
}
