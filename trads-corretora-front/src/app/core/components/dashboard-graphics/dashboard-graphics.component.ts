import { Component, computed, input } from '@angular/core';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';

@Component({
  selector: 'app-dashboard-graphics',
  imports: [BaseChartDirective],
  templateUrl: './dashboard-graphics.component.html',
  styleUrl: './dashboard-graphics.component.css',
})
export class DashboardGraphicsComponent {
  readonly data = input<{ name: string; value: number }[]>([]);
  readonly chartType = input<string>('bar-vertical');
  readonly xAxisLabel = input<string>('');
  readonly yAxisLabel = input<string>('');
  readonly showXAxis = input<boolean>(true);
  readonly showYAxis = input<boolean>(true);
  readonly showLegend = input<boolean>(true);
  readonly tooltipDisabled = input<boolean>(false);

  getChartType(type: string): ChartType {
    const map: Record<string, ChartType> = {
      'bar-vertical': 'bar',
      'bar-horizontal': 'bar',
      'pie': 'pie',
      'advanced-pie': 'doughnut',
      'line': 'line',
      'number-card': 'bar'
    };
    return map[type] || 'bar';
  }

  readonly chartData = computed<ChartData>(() => {
    const rawData = this.data() || [];
    return {
      labels: rawData.map(d => d.name),
      datasets: [
        {
          data: rawData.map(d => d.value),
          label: this.yAxisLabel() || 'Dados',
          backgroundColor: [
            '#5AA454', '#A10A28', '#C7B42C', '#AAAAAA', '#7aa3e5', '#a8385d', '#aae3f5',
            '#adcded', '#ffb84d', '#70db70', '#ff85a2', '#b380ff', '#4dffdb', '#ffd633',
            '#ff9966', '#99cc00', '#00b3b3', '#cc66ff', '#ff6666', '#999966'
          ],
        }
      ]
    };
  });

  readonly chartOptions = computed<ChartConfiguration['options']>(() => {
    const isBarOrLine = this.chartType().includes('bar') || this.chartType() === 'line';

    return {
      responsive: true,
      maintainAspectRatio: false,
      indexAxis: this.chartType() === 'bar-horizontal' ? 'y' : 'x',
      plugins: {
        legend: { display: this.showLegend() },
        tooltip: { enabled: !this.tooltipDisabled() }
      },
      scales: isBarOrLine ? {
        x: {
          display: this.showXAxis(),
          title: {
            display: !!this.xAxisLabel(),
            text: this.xAxisLabel(),
            color: '#9ca3af',
            font: { weight: 'bold' }
          }
        },
        y: {
          display: this.showYAxis(),
          beginAtZero: true,
          title: {
            display: !!this.yAxisLabel(),
            text: this.yAxisLabel(),
            color: '#9ca3af',
            font: { weight: 'bold' }
          }
        }
      } : {}
    };
  });
}
