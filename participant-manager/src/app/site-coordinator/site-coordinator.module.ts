import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SiteCoordinatorRoutingModule} from './site-coordinator-routing.module';
import {SiteCoordinatorComponent} from './sitecoordinator.component';
import {FormsModule} from '@angular/forms';
import {NgxDataTableModule} from 'angular-9-datatable';
import {MobileMenuComponent} from './mobile-menu/mobile-menu.component';
import {DashboardHeaderComponent} from './dashboard-header/dashboard-header.component';
import {ParticipantDetailsComponent} from './participant-details/participant-details.component';
import {SharedModule} from '../shared/shared.module';
import {RoleGuard} from '../../app/service/roles.guard';
import {LocationsGuard} from '../service/locations.guard';
import {LoadmoreSpinnerComponent} from './loadmore-spinner/loadmore-spinner.component';
import {BnNgIdleService} from 'bn-ng-idle';
@NgModule({
  declarations: [
    SiteCoordinatorComponent,
    MobileMenuComponent,
    DashboardHeaderComponent,
    ParticipantDetailsComponent,
    LoadmoreSpinnerComponent,
  ],
  imports: [
    CommonModule,
    SiteCoordinatorRoutingModule,
    FormsModule,
    NgxDataTableModule,
    SharedModule,
  ],
  providers: [RoleGuard, LocationsGuard, BnNgIdleService],
  exports: [LoadmoreSpinnerComponent],
})
export class SiteCoordinatorModule {}
