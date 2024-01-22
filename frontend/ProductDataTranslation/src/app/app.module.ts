import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {registerLocaleData} from '@angular/common';
import localeAf from '@angular/common/locales/af';
import localeDe from '@angular/common/locales/de';
import localeEn from '@angular/common/locales/en';
import localeFr from '@angular/common/locales/fr';
import localeBg from '@angular/common/locales/bg';
import localeCs from '@angular/common/locales/cs';
import localeDa from '@angular/common/locales/da';
import localeEl from '@angular/common/locales/el';
import localeEs from '@angular/common/locales/es';
import localeEt from '@angular/common/locales/et';
import localeFi from '@angular/common/locales/fi';
import localeHu from '@angular/common/locales/hu';
import localeId from '@angular/common/locales/id';
import localeIt from '@angular/common/locales/it';
import localeJa from '@angular/common/locales/ja';
import localeKo from '@angular/common/locales/ko';
import localeLt from '@angular/common/locales/lt';
import localeLv from '@angular/common/locales/lv';
import localeNb from '@angular/common/locales/nb';
import localeNl from '@angular/common/locales/nl';
import localePl from '@angular/common/locales/pl';
import localePt from '@angular/common/locales/pt';
import localeRo from '@angular/common/locales/ro';
import localeRu from '@angular/common/locales/ru';
import localeSk from '@angular/common/locales/sk';
import localeSl from '@angular/common/locales/sl';
import localeSv from '@angular/common/locales/sv';
import localeTr from '@angular/common/locales/tr';
import localeUk from '@angular/common/locales/uk';
import localeZh from '@angular/common/locales/zh';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatTableModule} from '@angular/material/table';
import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatSortModule} from '@angular/material/sort';
import {MatSelectModule} from '@angular/material/select';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatChipsModule} from '@angular/material/chips';
import {MatDialogModule} from '@angular/material/dialog';
import {MatSnackBarModule} from '@angular/material/snack-bar';

import {SearchPageComponent} from './components/search-page/search-page.component';
import {DetailPageComponent} from './components/detail-page/detail-page.component';
import {EditPageComponent} from './components/edit-page/edit-page.component';
import {CreatePageComponent} from './components/create-page/create-page.component';
import {PdtChooserComponent} from './components/pdt-chooser/pdt-chooser.component';
import {PdtPropertyViewComponent} from './components/pdt-property-view/pdt-property-view.component';
import {PdtDeletionConfirmationComponent} from './components/pdt-deletion-confirmation/pdt-deletion-confirmation.component';

import {FilterPipe} from './pipes/filter.pipe';
import {FindPipe} from './pipes/find.pipe';

@NgModule({
  declarations: [
    AppComponent,
    SearchPageComponent,
    DetailPageComponent,
    EditPageComponent,
    FilterPipe,
    FindPipe,
    CreatePageComponent,
    PdtChooserComponent,
    PdtPropertyViewComponent,
    PdtDeletionConfirmationComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MatSlideToggleModule,
    MatToolbarModule,
    MatButtonModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,
    MatTableModule,
    MatIconModule,
    MatMenuModule,
    MatPaginatorModule,
    MatSortModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatDialogModule,
    MatSnackBarModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor() {
    registerLocaleData(localeAf);
    registerLocaleData(localeDe);
    registerLocaleData(localeEn);
    registerLocaleData(localeFr);
    registerLocaleData(localeBg);
    registerLocaleData(localeCs);
    registerLocaleData(localeDa);
    registerLocaleData(localeEl);
    registerLocaleData(localeEs);
    registerLocaleData(localeEt);
    registerLocaleData(localeFi);
    registerLocaleData(localeHu);
    registerLocaleData(localeId);
    registerLocaleData(localeIt);
    registerLocaleData(localeJa);
    registerLocaleData(localeKo);
    registerLocaleData(localeLt);
    registerLocaleData(localeLv);
    registerLocaleData(localeNb);
    registerLocaleData(localeNl);
    registerLocaleData(localePl);
    registerLocaleData(localePt);
    registerLocaleData(localeRo);
    registerLocaleData(localeRu);
    registerLocaleData(localeSk);
    registerLocaleData(localeSl);
    registerLocaleData(localeSv);
    registerLocaleData(localeTr);
    registerLocaleData(localeUk);
    registerLocaleData(localeZh);
  }
}
