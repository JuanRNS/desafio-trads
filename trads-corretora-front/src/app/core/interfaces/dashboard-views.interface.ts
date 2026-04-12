/**
 * Representa a visão de fechamentos com ganho (vw_fechamento_ganho)
 */
export interface VwFechamentoGanho {
  id_negocio: number;
  pipeline_id: number;
  data_ganho: string;
  opportunity: number;
  currency_id: string;
  assigned_user_id: number;
}

/**
 * Representa a visão de fechamentos perdidos (vw_fechamento_perda)
 */
export interface VwFechamentoPerda {
  id_negocio: number;
  pipeline_id: number;
  data_perda: string;
  opportunity: number;
  currency_id: string;
  assigned_user_id: number;
}

/**
 * Representa a visão de tempo em cada etapa (vw_tempo_em_etapa)
 */
export interface VwTempoEmEtapa {
  id_negocio: number;
  stage_id: string;
  pipeline_id: number;
  inicio: string;
  fim: string | null;
  dias_na_etapa: number;
}

/**
 * Representa a visão das transições de funil (vw_transicoes)
 */
export interface VwTransicoes {
  id_movimentacao: number;
  id_negocio: number;
  pipeline_id: number;
  stage_from: string | null;
  stage_to: string;
  t_transicao: string;
  sort_from: number | null;
  sort_to: number;
}
