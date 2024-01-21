import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchPageComponent } from './components/search-page/search-page.component';
import { DetailPageComponent } from './components/detail-page/detail-page.component';
import { EditPageComponent } from './components/edit-page/edit-page.component';
import { CreatePageComponent } from './components/create-page/create-page.component';

const routes: Routes = [
  { path: '', redirectTo: '/search', pathMatch: 'full' },
  { path: 'search', component: SearchPageComponent },
  { path: 'create', component: CreatePageComponent },
  { path: 'product/:id', component: DetailPageComponent },
  { path: 'edit/:id', component: EditPageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
