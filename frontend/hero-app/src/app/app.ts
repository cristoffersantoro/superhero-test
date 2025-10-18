import { Component, signal } from '@angular/core';
import { RouterOutlet, RouterModule } from '@angular/router';

// PrimeNG v20
import { DrawerModule } from 'primeng/drawer';
import { PanelMenuModule } from 'primeng/panelmenu';
import { ButtonModule } from 'primeng/button';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterModule,
    DrawerModule,      // <= substitui o antigo SidebarModule
    PanelMenuModule,
    ButtonModule
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('hero-app');

  drawerVisible = false;

  items: MenuItem[] = [
    { label: 'Início', icon: 'pi pi-home', routerLink: '/' },
    { label: 'Heróis', icon: 'pi pi-users', routerLink: '/heroes' }
  ];
}
