import { Component } from '@angular/core';
import { CryptiesListComponent } from './crypties-list/crypties-list.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: true,
  imports: [CryptiesListComponent]
})
export class AppComponent {
  title = 'crypto-angular';
}
