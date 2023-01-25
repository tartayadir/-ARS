import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { TooltipModule } from 'ngx-bootstrap/tooltip/tooltip.module';

import { AppComponent } from './app.component';
import {CarsComponent} from "./components/page-elements/cars/cars.component";
import {CarDetailsPageComponent} from "./components/pages/car-details-page/car-details-page.component";
import {LoginPageComponent} from "./components/pages/login-page/login-page.component";
import {AddCarPageComponent} from "./components/pages/add-car-page/add-car-page.component";
import {AuthGuard} from "./services/auth/auth.guard";
import {CarEditorPageComponent} from "./components/pages/car-editor-page/car-editor-page.component";
import {RouterModule, Routes} from "@angular/router";
import {CarComponent} from "./components/page-elements/car/car.component";
import {NavbarComponent} from "./components/page-elements/navbar/navbar.component";
import {LoginButtonComponent} from "./components/buttons/login-button/login-button.component";
import {LogoutButtonComponent} from "./components/buttons/logout-button/logout-button.component";
import {GlobalErrorComponent} from "./components/global-error/global-error.component";
import {AddCarButtonComponent} from "./components/buttons/add-car-buttom/add-car-button.component";
import {DeleteCarButtonComponent} from "./components/buttons/delete-car-button/delete-car-button.component";
import {EditCarButtonComponent} from "./components/buttons/edit-car-button/edit-car-button.component";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {CarsService} from "./services/cars.service";
import {httpInterceptorProviders} from "./services/auth/auth-interceptor";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ComeBackButtonComponent } from './components/buttons/come-back-button/come-back-button.component';
import { ModalWindowComponent } from './components/page-elements/modal-window/modal-window.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ConfirmDeleteWindowComponent } from './components/page-elements/confirm-delete-window/confirm-delete-window.component';
import { ConfirmDeleteCarButtonComponent } from './components/buttons/confirm-delete-car-button/confirm-delete-car-button.component';
import { MdbAccordionModule } from 'mdb-angular-ui-kit/accordion';
import { MdbCarouselModule } from 'mdb-angular-ui-kit/carousel';
import { MdbCheckboxModule } from 'mdb-angular-ui-kit/checkbox';
import { MdbCollapseModule } from 'mdb-angular-ui-kit/collapse';
import { MdbDropdownModule } from 'mdb-angular-ui-kit/dropdown';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { MdbModalModule } from 'mdb-angular-ui-kit/modal';
import { MdbPopoverModule } from 'mdb-angular-ui-kit/popover';
import { MdbRadioModule } from 'mdb-angular-ui-kit/radio';
import { MdbRangeModule } from 'mdb-angular-ui-kit/range';
import { MdbRippleModule } from 'mdb-angular-ui-kit/ripple';
import { MdbScrollspyModule } from 'mdb-angular-ui-kit/scrollspy';
import { MdbTabsModule } from 'mdb-angular-ui-kit/tabs';
import { MdbTooltipModule } from 'mdb-angular-ui-kit/tooltip';
import { MdbValidationModule } from 'mdb-angular-ui-kit/validation';
import { ImageCropperModule } from 'ngx-image-cropper';
import { TesrComponent } from './components/tesr/tesr.component';
import {AngularSvgIconModule} from "angular-svg-icon";
import {MatChipsModule} from "@angular/material/chips";
import {MatIconModule} from "@angular/material/icon";
import {MatFormFieldModule} from "@angular/material/form-field";
import {A11yModule} from "@angular/cdk/a11y";
import { ToTopButtonComponent } from './components/buttons/to-top-button/to-top-button.component';

const appRoutes: Routes = [
  { path: '', component: CarsComponent },
  { path: 'details/:id', component: CarDetailsPageComponent},
  { path: 'login-page', component: LoginPageComponent},
  { path: 'add-car-page', component: AddCarPageComponent, canActivate: [AuthGuard]},
  { path: 'update/:id', component: CarEditorPageComponent, canActivate: [AuthGuard]},
  { path: '**', redirectTo: '' }
]

@NgModule({
  declarations: [
    AppComponent,
    CarComponent,
    NavbarComponent,
    CarsComponent,
    CarDetailsPageComponent,
    CarEditorPageComponent,
    LoginPageComponent,
    LoginButtonComponent,
    LogoutButtonComponent,
    GlobalErrorComponent,
    AddCarButtonComponent,
    AddCarPageComponent,
    DeleteCarButtonComponent,
    EditCarButtonComponent,
    ComeBackButtonComponent,
    ModalWindowComponent,
    ConfirmDeleteWindowComponent,
    ConfirmDeleteCarButtonComponent,
    TesrComponent,
    ToTopButtonComponent
  ],
    imports: [
        TooltipModule.forRoot(),
        BrowserModule,
        HttpClientModule,
        RouterModule.forRoot(appRoutes, {onSameUrlNavigation: `reload`}),
        ReactiveFormsModule,
        FormsModule,
        FormsModule,
        NgbModule,
        BrowserAnimationsModule,
        MdbAccordionModule,
        MdbCarouselModule,
        MdbCheckboxModule,
        MdbCollapseModule,
        MdbDropdownModule,
        MdbFormsModule,
        MdbModalModule,
        MdbPopoverModule,
        MdbRadioModule,
        MdbRangeModule,
        MdbRippleModule,
        MdbScrollspyModule,
        MdbTabsModule,
        MdbTooltipModule,
        MdbValidationModule,
        ImageCropperModule,
        AngularSvgIconModule.forRoot(),
        MatChipsModule,
        MatIconModule,
        MatFormFieldModule,
        A11yModule
    ],
  providers: [
    CarsService, httpInterceptorProviders
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
