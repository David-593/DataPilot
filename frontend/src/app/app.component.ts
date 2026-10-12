import { CommonModule, CurrencyPipe, DecimalPipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SalesAnalyticsService } from './sales-analytics.service';
import { SalesSummary } from './sales-summary.model';

@Component({
  selector: 'app-root', standalone: true,
  imports: [CommonModule, FormsModule, CurrencyPipe, DecimalPipe],
  template: `
    <main class="page">
      <header class="topbar">
        <div><p class="eyebrow">NOVACOMMERCE · ANALÍTICA COMERCIAL</p><h1>DataPilot</h1><p class="subtitle">Resumen de ventas para tomar decisiones con datos.</p></div>
        <span class="status"><span class="dot"></span> Panel de desarrollo</span>
      </header>
      <section class="filters">
        <label>Desde<input type="date" [(ngModel)]="fromDate"></label>
        <label>Hasta (exclusivo)<input type="date" [(ngModel)]="toDate"></label>
        <button (click)="load()" [disabled]="loading">{{ loading ? 'Consultando…' : 'Actualizar informe' }}</button>
      </section>
      <p class="error" *ngIf="error">{{ error }}</p>
      <section class="metrics" *ngIf="summary as data">
        <article class="metric-card"><p class="metric-label">Total vendido</p><p class="metric-value">{{ data.totalSales | currency:'USD':'symbol':'1.2-2' }}</p><p class="metric-help">Suma de detalles de compras completadas</p></article>
        <article class="metric-card"><p class="metric-label">Compras realizadas</p><p class="metric-value">{{ data.purchaseCount | number }}</p><p class="metric-help">Cada compra se cuenta una sola vez</p></article>
        <article class="metric-card"><p class="metric-label">Unidades vendidas</p><p class="metric-value">{{ data.unitsSold | number }}</p><p class="metric-help">Suma de cantidades de los artículos</p></article>
      </section>
      <section class="notice"><h2>Qué estás viendo</h2><p>El dashboard consume la API de Spring Boot. El backend consulta PostgreSQL y aplica un rango de fechas con límite final exclusivo.</p><code>GET /api/analytics/sales?from=2026-10-01&amp;to=2026-11-01</code></section>
      <footer>DataPilot · Proyecto de aprendizaje de ingeniería de software</footer>
    </main>
  `
})
export class AppComponent implements OnInit {
  private readonly salesService = inject(SalesAnalyticsService);
  fromDate = '2026-10-01';
  toDate = '2026-11-01';
  summary: SalesSummary | null = null;
  loading = false;
  error = '';

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.error = '';
    this.salesService.getSales(this.fromDate, this.toDate).subscribe({
      next: (result) => { this.summary = result; this.loading = false; },
      error: () => {
        this.error = 'No se pudo conectar con la API. Comprueba que PostgreSQL y Spring Boot estén ejecutándose.';
        this.loading = false;
      }
    });
  }
}
