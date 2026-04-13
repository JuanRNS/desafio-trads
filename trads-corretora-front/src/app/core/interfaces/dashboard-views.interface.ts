export interface IPageResponse<T> {
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

export interface IVwGain {
  id: number;
  idNegocio: number;
  pipelineId: number;
  winDate: string;
  opportunity: number;
  currencyId: string;
  userId: number;
}

export interface IVwLoss {
  id: number;
  idNegocio: number;
  pipelineId: number;
  lossDate: string;
  opportunity: number;
  currencyId: string;
  userId: number;
}

export interface IVwTimeStage {
  id: number;
  idNegocio: number;
  pipelineId: number;
  stageId: string;
  start: string;
  end: string;
  daysInStage: number;
}

export interface IVwTransitions {
  id: number;
  idNegocio: number;
  pipelineId: number;
  stageFrom: string;
  stageTo: string;
  transition: string;
  sortTo: number;
  sortFrom: number;
}

export interface IVwLossConversion {
  statusId: String,
  stageFrom: String,
  count: number
}

export interface IUsers {
  id: number;
  name: string;
}

export interface IStage {
  id: number;
  statusId: string;
}

