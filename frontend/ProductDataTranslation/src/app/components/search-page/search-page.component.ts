import { Component } from '@angular/core';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrl: './search-page.component.scss'
})
export class SearchPageComponent {
  displayedColumns: string[] = ['position', 'name', 'action']
  datasource = [
    { position: 1, name: "Hammer" },
    { position: 2, name: "Sichel" },
    { position: 3, name: "Meißel" },
    { position: 4, name: "Bohrer" },
    { position: 5, name: "Dreher" },
  ]
}
