import { Component, OnInit } from '@angular/core';
import { Crypto } from '../crypto';
import { CryptoService } from '../crypto.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-crypties-list',
  templateUrl: './crypties-list.component.html',
  styleUrls: ['./crypties-list.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class CryptiesListComponent implements OnInit {
  crypties: Crypto[] = [];
  filteredCrypties: Crypto[] = [];
  paginatedCrypties: Crypto[] = [];
  currentPage: number = 1;
  totalPages: number = 1;
  pageSize: number = 10;
  isPriceAscending: boolean = true;
  sortBy: string = 'default';

  isLoading: boolean = false;
  errorMessage: string | null = null;

  filters = {
    symbol: '',
    minPrice: null,
    maxPrice: null
  };

  constructor(private cryptoService: CryptoService) { }

  ngOnInit(): void {
    this.getCrypties();
  }

  getCrypties(): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.cryptoService.getCrypties(this.filters).subscribe(
      (data: Crypto[]) => {
        this.crypties = data || [];
        this.applyPagination();
        this.isLoading = false;
      },
      (error) => {
        this.isLoading = false;
        console.error('Error fetching cryptocurrency data:', error);
        this.errorMessage = error.message || 'Error loading data. Please try again later.';
      }
    );
  }


  applyPagination(): void {
    this.totalPages = Math.ceil(this.crypties.length / this.pageSize);
    this.updatePaginatedCrypties();
  }

  updatePaginatedCrypties(): void {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = Math.min(startIndex + this.pageSize, this.crypties.length);
    this.paginatedCrypties = this.crypties.slice(startIndex, endIndex);
  }

  changePage(page: number): void {
    if (page > 0 && page <= this.totalPages) {
      this.currentPage = page;
      this.updatePaginatedCrypties();
    }
  }

  sortByField(): void {
    if (this.sortBy === 'default') {
        return;
    }
    if (this.sortBy === 'symbol') {
      this.crypties.sort((a, b) => a.symbol.localeCompare(b.symbol));
    } else if (this.sortBy === 'name') {
      this.crypties.sort((a, b) => a.name.localeCompare(b.name));
    } else if (this.sortBy === 'priceAsc') {
      this.crypties.sort((a, b) => a.current_price - b.current_price);
    } else if (this.sortBy === 'priceDesc') {
      this.crypties.sort((a, b) => b.current_price - a.current_price);
    }
    this.applyPagination();
  }

  applyFilters(): void {
    this.currentPage = 1;
    this.getCrypties();
  }

  clearFilters(): void {
    this.filters = {
      symbol: '',
      minPrice: null,
      maxPrice: null
    };
    this.getCrypties();
  }
}
