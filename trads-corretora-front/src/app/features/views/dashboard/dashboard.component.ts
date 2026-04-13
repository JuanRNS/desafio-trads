import { Component, inject, computed, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { DashboardGraphicsComponent } from '../../../core/components/dashboard-graphics/dashboard-graphics.component';
import { DashboardService } from '../../services/dashboard.service';
import { HeaderComponent } from "../../../core/components/header/header.component";
import { AuthService } from '../../services/auth.service';
import { IVwGain, IVwLoss, IVwTimeStage, IVwTransitions, IVwLossConversion, IUsers } from '../../../core/interfaces/dashboard-views.interface';
import verifyTransitions from '../../../core/utils/verify-transitions';

@Component({
  selector: 'app-dashboard',
  imports: [DashboardGraphicsComponent, HeaderComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {
  private dashboardService = inject(DashboardService);
  public authService = inject(AuthService);
  public role = this.authService.currentUserRole();

  public gains = signal<IVwGain[]>([]);
  public losses = signal<IVwLoss[]>([]);
  public timeStages = signal<IVwTimeStage[]>([]);
  public transitions = signal<IVwTransitions[]>([]);
  public lossConversions = signal<IVwLossConversion[]>([]);
  public users = signal<IUsers[]>([]);
  public dateFilter = signal<{ start: Date, end: Date } | null>(null);
  public filterType = signal<'day' | 'week' | 'month'>('month');
  public transitionsFilter = signal<'ALL' | 'P0' | 'P10' | 'P20'>('ALL');
  public stagesFilter = signal<'ALL' | 'P0' | 'P10' | 'P20'>('ALL');
  public selectedUserId = signal<number | null>(null);

  ngOnInit(): void {
    if (this.role === 'ADMIN' || this.role === 'MANAGER') {
      this.dashboardService.getAllUsers().subscribe({
        next: (res) => this.users.set(res),
        error: (err) => console.error('Erro ao buscar Usuários:', err)
      });
    }
    this.loadDashboardData();
  }

  public onUserChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    const value = target.value;
    this.selectedUserId.set(value ? Number(value) : null);
    this.loadDashboardData();
  }

  private loadDashboardData(): void {
    const currentUserId = this.selectedUserId() ?? undefined;
    const start = this.dateFilter()?.start.toISOString() ?? undefined;
    const end = this.dateFilter()?.end.toISOString() ?? undefined;

    this.dashboardService.getGains(currentUserId, start, end).subscribe({
      next: (res) => {
        this.gains.set(res.content || []);
      },
      error: (err) => console.error('Erro ao buscar Ganhos:', err)
    });

    this.dashboardService.getLosses(currentUserId, start, end).subscribe({
      next: (res) => {
        this.losses.set(res.content || []);
      },
      error: (err) => console.error('Erro ao buscar Perdas:', err)
    });

    this.dashboardService.getTimeStages(currentUserId, start, end).subscribe({
      next: (res) => {
        this.timeStages.set(res.content || []);
      },
      error: (err) => console.error('Erro ao buscar Time Stages:', err)
    });

    this.dashboardService.getTransitions(currentUserId, start, end).subscribe({
      next: (res) => {
        this.transitions.set(res.content || []);
      },
      error: (err) => console.error('Erro ao buscar Transitions:', err)
    });

    this.dashboardService.getLossConversion(currentUserId, start, end).subscribe({
      next: (res) => {
        this.lossConversions.set(res || []);
      },
      error: (err) => console.error('Erro ao buscar Loss Conversion:', err)
    });
  }

  public applyDateFilter(start: string, end: string): void {
    if (start && end) {
      const endDate = new Date(end);
      endDate.setHours(23, 59, 59, 999);
      this.dateFilter.set({ start: new Date(start), end: endDate });
      this.loadDashboardData();
    } else {
      this.dateFilter.set(null);
      this.loadDashboardData();
    }
  }
  public lineChartData = computed(() => {
    const contentGains = this.gains();
    const groups: { [key: string]: number } = {};

    contentGains.forEach(g => {
      const date = new Date(g.winDate);
      let key = '';
      switch (this.filterType()) {
        case 'day':
          key = date.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric' });
          break;
        case 'week':
          const dayOfWeek = date.getDay();
          const firstDayOfWeek = new Date(date);
          firstDayOfWeek.setDate(date.getDate() - dayOfWeek);
          firstDayOfWeek.setHours(0, 0, 0, 0);

          key = `Semana de ${firstDayOfWeek.toLocaleDateString('pt-BR', {
            day: '2-digit',
            month: '2-digit'
          })}`;
          break;
        case 'month':
          key = date.toLocaleDateString('pt-BR', { month: 'long', year: 'numeric' });
          break;
      }

      key = key.charAt(0).toUpperCase() + key.slice(1);

      groups[key] = (groups[key] || 0) + (g.opportunity || 0);
    });

    return Object.entries(groups).map(([name, value]) => ({
      name: name,
      value: value
    })).sort((a, b) => {
      const aDate = this.parseDate(a.name);
      const bDate = this.parseDate(b.name);
      return aDate.getTime() - bDate.getTime();
    });
  });

  public barChartData = computed(() => {
    const content = this.timeStages();
    const stageDays = content.reduce((acc: Record<string, { totalDays: number, count: number }>, stage: IVwTimeStage) => {
      const stageName = verifyTransitions(stage.stageId) || 'Desconhecido';
      if (!acc[stageName]) acc[stageName] = { totalDays: 0, count: 0 };
      acc[stageName].totalDays += (stage.daysInStage || 0);
      acc[stageName].count += 1;
      return acc;
    }, {} as Record<string, { totalDays: number, count: number }>);

    let data = Object.entries(stageDays).map(([name, data]) => ({
      name: `Etapa ${name}`,
      value: Math.round(data.totalDays / data.count)
    }));

    const filter = this.stagesFilter();
    if (filter !== 'ALL') {
      data = data.filter(item => item.name.includes(`[${filter}]`));
    }

    return data.sort((a, b) => this.getStageWeigth(a.name) - this.getStageWeigth(b.name));
  });

  public transitionsChartData = computed(() => {
    const content = this.transitions();
    const transitionCounts = content.reduce((acc: Record<string, number>, trans: IVwTransitions) => {
      const label = `${verifyTransitions(trans.stageFrom)} → ${verifyTransitions(trans.stageTo)}`;
      acc[label] = (acc[label] || 0) + 1;
      return acc;
    }, {});

    let data = Object.entries(transitionCounts)
      .map(([name, value]) => ({ name, value: value as number }));

    const filter = this.transitionsFilter();
    if (filter !== 'ALL') {
      data = data.filter(item => item.name.includes(`[${filter}]`));
    }

    return data.sort((a, b) => {
      const weightA = this.getTransitionWeigth(a.name);
      const weightB = this.getTransitionWeigth(b.name);

      if (weightA !== weightB) {
        return weightA - weightB;
      }

      return b.value - a.value;
    })
  });

  public lossConversionChartData = computed(() => {
    const content = this.lossConversions();
    return content.map(item => ({
      name: `${verifyTransitions(item.stageFrom as string)} → ${verifyTransitions(item.statusId as string)}` || 'Desconhecido',
      value: item.count
    }));
  });

  private parseDate(dateString: string): Date {
    const months = {
      'Janeiro': 0, 'Fevereiro': 1, 'Março': 2, 'Abril': 3, 'Maio': 4, 'Junho': 5,
      'Julho': 6, 'Agosto': 7, 'Setembro': 8, 'Outubro': 9, 'Novembro': 10, 'Dezembro': 11
    };

    const [monthName, yearStr] = dateString.split(' de ');
    const month = months[monthName as keyof typeof months];
    const year = parseInt(yearStr);

    return new Date(year, month, 1);
  }

  private getTransitionWeigth(label: string): number {
    const [from, to] = label.split(' → ');
    const fromVal = this.extractNumber(from);
    const toVal = this.extractNumber(to);
    return (fromVal * 100) + toVal;
  }

  private extractNumber(value: string): number {
    const match = value.match(/\d+/);
    return match ? parseInt(match[0]) : 0;
  }

  private getStageWeigth(label: string): number {
    const value = label.split(' ')[1];
    const number = this.extractNumber(value);
    return number;
  }

}

