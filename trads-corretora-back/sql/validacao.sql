
-- 1. **Órfãos em movimentações:** `id_negocio` que não existe em `negocios`.
SELECT m.* FROM movimentacoes m
                    LEFT JOIN negocios n ON m.id_negocio = n.id
WHERE m.id_negocio IS NULL;


-- 2. **Pipeline/etapa inválidos:** `stage_id` + `pipeline_id` sem linha em `etapas`.
SELECT m.* FROM movimentacoes m
                    LEFT JOIN stages s ON m.stage_id = s.status_id AND m.pipeline_id = s.pipeline_id
WHERE s.id ISNULL;

-- 3. **Usuários:** `assigned_user_id` e `created_user_id` existem em `usuarios`.
SELECT n.* FROM negocios n
                    LEFT JOIN usuarios u ON n.assigned_user_id = u.id
                    LEFT JOIN usuarios u2 ON n.created_user_id = u2.id
WHERE u.id IS NULL OR u2.id IS NULL;

-- 4. **Snapshot = última movimentação:** para cada negócio, comparar `negocios.stage_id` e `negocios.pipeline_id` com a última linha de `movimentacoes` (ordenar por `created_time`, desempate `id`).
SELECT n.id AS negocio_id,
       n.stage_id AS stage_negocio,
       ultima_mov.stage_id AS stage_ultima_mov
FROM negocios n
         INNER JOIN (
    SELECT m1.*
    FROM movimentacoes m1
    WHERE m1.id = (
        SELECT m2.id
        FROM movimentacoes m2
        WHERE m2.id_negocio = m1.id_negocio
        ORDER BY m2.created_time DESC, m2.id DESC
        LIMIT 1
)
    ) AS ultima_mov ON n.id = ultima_mov.id_negocio
WHERE n.stage_id = ultima_mov.stage_id
OR n.pipeline_id = ultima_mov.pipeline_id;
-- 5. **Cronologia:** nenhuma `movimentacoes.created_time` menor que `negocios.date_create` do mesmo deal.

SELECT m.* FROM movimentacoes m
                    LEFT JOIN negocios n ON m.id_negocio = n.id
WHERE m.created_time < n.date_create;

-- 6. **Monotonicidade:** para cada `id_negocio`, `created_time` não decrescente (salvo se você modelar correções explícitas; neste projeto, não há).

WITH Sequencia AS (
    SELECT
        id_negocio,
        created_time,
        LAG(created_time) OVER (PARTITION BY id_negocio ORDER BY created_time) AS tempo_anterior
    FROM movimentacoes
)
SELECT * FROM Sequencia
WHERE created_time < tempo_anterior;