export interface PageResponse<T> {
  content: T[];
  pageable: any;
  totalPages: number;
  totalElements: number;
  last: boolean;
  size: number;
  number: number;
  sort: any;
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}

export interface VwGainDTO {
  id: number;
  idNegocio: number;
  pipelineId: number;
  winDate: string;
  opportunity: number;
  currencyId: string;
  userId: number;
}

export interface VwLossDTO {
  id: number;
  idNegocio: number;
  pipelineId: number;
  lossDate: string;
  opportunity: number;
  currencyId: string;
  userId: number;
}

export interface VwTimeStageDTO {
  id: number;
  idNegocio: number;
  pipelineId: number;
  stageId: string;
  start: string;
  end: string;
  daysInStage: number;
}

export interface VwTransitionsDTO {
  id: number;
  idNegocio: number;
  pipelineId: number;
  stageFrom: string;
  stageTo: string;
  transition: string;
  sortTo: number;
  sortFrom: number;
}

export interface VwLossConversion {
  statusId: String,
  stageFrom: String,
  count: number
}

export interface UsersDTO {
  id: number;
  name: string;
}

export interface StageDTO {
  id: number;
  statusId: string;
}

