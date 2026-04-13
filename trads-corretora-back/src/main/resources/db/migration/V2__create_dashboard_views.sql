CREATE VIEW IF NOT EXISTS vw_transicoes AS
WITH valores AS (
    SELECT
        m.id AS id_movimentacao,
        m.id_negocio,
        m.pipeline_id,
        LAG(m.stage_id) OVER (PARTITION BY m.id_negocio ORDER BY m.created_time, m.id) AS stage_from,
        m.stage_id AS stage_to,
        m.created_time AS t_transicao,
        LAG(s.sort) OVER (PARTITION BY m.id_negocio ORDER BY m.created_time, m.id) AS sort_from,
        s.sort AS sort_to,
        n.assigned_user_id
    FROM movimentacoes m
             INNER JOIN stages s ON m.stage_id = s.status_id AND m.pipeline_id = s.pipeline_id
             INNER JOIN negocios n ON m.id_negocio = n.id
)
SELECT * FROM valores
WHERE stage_from IS NOT NULL
  AND sort_from IS NOT NULL;

CREATE VIEW IF NOT EXISTS vw_fechamento_ganho AS
SELECT
    m.id AS id_movimentacao,
    m.id_negocio,
    m.pipeline_id,
    MAX(m.created_time) AS data_ganho,
    n.opportunity,
    n.currency_id,
    n.assigned_user_id
FROM movimentacoes m
         INNER JOIN stages s ON m.stage_id = s.status_id
         INNER JOIN negocios n ON m.id_negocio = n.id
WHERE s.semantics = 'S'
  AND n.closed = 1 -- No SQLite usamos 1 para TRUE
GROUP BY m.id_negocio, n.opportunity;

CREATE VIEW IF NOT EXISTS vw_fechamento_perda AS
SELECT
    m.id AS id_movimentacao,
    m.id_negocio,
    m.pipeline_id,
    MAX(m.created_time) AS data_perda,
    n.opportunity,
    n.currency_id,
    n.assigned_user_id
FROM movimentacoes m
         INNER JOIN stages s ON m.stage_id = s.status_id
         INNER JOIN negocios n ON m.id_negocio = n.id
WHERE s.semantics = 'F'
  AND n.closed = 1
GROUP BY m.id_negocio, n.opportunity;

CREATE VIEW IF NOT EXISTS vw_loss_conversion AS
SELECT
    s.status_id,
    vw.stage_from,
    vw.id_movimentacao,
    vw.assigned_user_id,
    vw.t_transicao,
    COUNT(*) AS total_transicoes
FROM vw_transicoes vw
         INNER JOIN stages s ON vw.stage_to = s.status_id
WHERE s.semantics = 'F'
GROUP BY vw.stage_from;

CREATE VIEW IF NOT EXISTS vw_tempo_em_etapa AS
WITH tempo_etapa AS (
    SELECT
        m.id AS id_movimentacao,
        m.id_negocio,
        m.stage_id,
        m.pipeline_id,
        m.created_time AS inicio,
        n.assigned_user_id,
        COALESCE(
                LEAD(m.created_time) OVER (PARTITION BY m.id_negocio ORDER BY m.created_time),
                strftime('%s', 'now') * 1000
        ) AS fim
    FROM movimentacoes m
             INNER JOIN negocios n ON n.id = m.id_negocio
)
SELECT
    *,
    ABS(CAST((fim - inicio) AS REAL) / 86400000.0) AS dias_na_etapa
FROM tempo_etapa;