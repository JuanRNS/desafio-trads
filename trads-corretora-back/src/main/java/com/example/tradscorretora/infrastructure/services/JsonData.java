package com.example.tradscorretora.infrastructure.services;

import com.example.tradscorretora.domain.entity.*;
import com.example.tradscorretora.infrastructure.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JsonData implements CommandLineRunner {

    private final PipelineRepository pipelineRepository;
    private final StageRepository stageRepository;
    private final UserRepository userRepository;
    private final BusinessDealRepository businessDealRepository;
    private final MovementRepository movementRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonData(PipelineRepository pipelineRepository, StageRepository stageRepository, UserRepository userRepository, BusinessDealRepository businessDealRepository, MovementRepository movementRepository) {
        this.pipelineRepository = pipelineRepository;
        this.stageRepository = stageRepository;
        this.userRepository = userRepository;
        this.businessDealRepository = businessDealRepository;
        this.movementRepository = movementRepository;
    }


    @Override
    public void run(String... args) {
        importPipelines();
        importStages();
        importUsers();
        importDeals();
        importMovements();
//        if (pipelineRepository.count() == 0) {
//            System.out.println("Importando dados do JSON para o banco de dados...");
//            importPipelines();
//            importStages();
//            importUsers();
//        }else{
//            System.out.println("Dados já existem no banco, pulando importação.");
//        }

    }

    private void importPipelines() {
        InputStream json = getClass().getResourceAsStream("/Banco/pipelines.json");
        List<PipelineEntity> pipelines = objectMapper.readValue(json, new TypeReference<>(){});
        pipelineRepository.saveAll(pipelines);
        System.out.println("✅ Pipelines importados.");
    }

    private void importStages() {

        List<StageEntity> stagesToSave = new ArrayList<>();
        JsonNode stagesJson = objectMapper.readTree(TypeReference.class.getResourceAsStream("/Banco/etapas.json"));

        Map<Long, PipelineEntity> pipelineMap = pipelineRepository.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(PipelineEntity::getId, pipeline -> pipeline));

        for (JsonNode node : stagesJson) {
            StageEntity stage = new StageEntity();
            stage.setId(node.get("id").asLong());
            stage.setStatusId(node.get("status_id").asString());
            stage.setNome(node.get("nome").asString());

            Long pipeId = node.get("pipeline_id").asLong();
            PipelineEntity pipe = pipelineMap.get(pipeId);
            stage.setSort(node.get("sort").asInt());
            stage.setSemantics(node.get("semantics").asString());

            if (pipe != null) {
                stage.setPipeline(pipe);
                stagesToSave.add(stage);
            }
        }

        stageRepository.saveAll(stagesToSave);
        System.out.println("Etapas importados com sucesso!");
    }

    private void importUsers() {
        InputStream json = getClass().getResourceAsStream("/Banco/usuarios.json");
        List<UserEntity> users = objectMapper.readValue(json, new TypeReference<>() {
        });
        userRepository.saveAll(users);
        System.out.println("✅ Usuários importados.");
    }

    private void importDeals() {
        List<BusinessDealEntity> dealsToSave = new ArrayList<>();
        JsonNode dealsJson = objectMapper.readTree(TypeReference.class.getResourceAsStream("/Banco/negocios.json"));
        Map<Long, PipelineEntity> pipelineMap = pipelineRepository.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(PipelineEntity::getId, pipeline -> pipeline));
        Map<Long, UserEntity> userMap = userRepository.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(UserEntity::getId, u -> u));
        Map<String, StageEntity> stageMap = stageRepository.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(StageEntity::getStatusId, s -> s));

        for (JsonNode node : dealsJson) {
            BusinessDealEntity deal = new BusinessDealEntity();
            deal.setId(node.get("id_negocio").asLong());
            deal.setTitulo(node.get("titulo").asString());
            deal.setOpportunity(node.get("opportunity").asDecimal());
            deal.setCurrencyId(node.get("currency_id").asString());
            deal.setProbability(node.get("probability").asInt());
            deal.setStageSemantics(node.get("stage_semantics").asString());
            deal.setDateCreate(LocalDateTime.parse(node.get("date_create").asString()));
            deal.setDateModify(LocalDateTime.parse(node.get("date_modify").asString()));
            deal.setMovedTime(LocalDateTime.parse(node.get("moved_time").asString()));
            deal.setClosed(node.get("closed").asBoolean());
            if (node.get("closedate") != null && !node.get("closedate").isNull()) {
                deal.setCloseDate(LocalDate.parse(node.get("closedate").asString()));
            }else{
                deal.setCloseDate(null);
            }
            deal.setUtmSource(node.get("utm_source").asString(null));
            deal.setUtmMedium(node.get("utm_medium").asString(null));
            deal.setUtmCampaign(node.get("utm_campaign").asString(null));
            deal.setComments(node.get("comments").asString());
            deal.setCustomFields(node.get("custom_fields").toString());

            Long pipeId = node.get("pipeline_id").asLong();
            Long userId = node.get("assigned_user_id").asLong();
            String stageId = node.get("stage_id").asString();

            PipelineEntity pipe = pipelineMap.get(pipeId);
            UserEntity userEntity = userMap.get(userId);
            StageEntity stageEntity = stageMap.get(stageId);

            if (pipe != null) {
                deal.setPipeline(pipe);
            }
            if (userEntity != null) {
                deal.setAssignedUser(userEntity);
                deal.setCreatedUser(userEntity);
            }
            if (stageEntity != null) {
                deal.setStage(stageEntity);
            }

            dealsToSave.add(deal);
        }
        businessDealRepository.saveAll(dealsToSave);
        System.out.println("Negócios importados com sucesso!");

    }

    private void importMovements() {
        List<MovementEntity> movementsToSave = new ArrayList<>();
        JsonNode movementsJson = objectMapper.readTree(TypeReference.class.getResourceAsStream("/Banco/movimentacoes.json"));
        Map<Long, PipelineEntity> pipelineMap = pipelineRepository.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(PipelineEntity::getId, pipeline -> pipeline));
        Map<Long, BusinessDealEntity> dealMap = businessDealRepository.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(BusinessDealEntity::getId, d -> d));
        Map<String, StageEntity> stageMap = stageRepository.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(StageEntity::getStatusId, s -> s));


        for (JsonNode node : movementsJson) {
            MovementEntity movement = new MovementEntity();
            movement.setId(node.get("id").asLong());
            movement.setCreatedTime(LocalDateTime.parse(node.get("created_time").asString()));
            movement.setTypeId(node.get("type_id").asInt());


            Long pipeId = node.get("pipeline_id").asLong();
            Long dealId = node.get("id_negocio").asLong();
            String stageId = node.get("stage_id").asString();

            PipelineEntity pipe = pipelineMap.get(pipeId);
            BusinessDealEntity deal = dealMap.get(dealId);
            StageEntity stage = stageMap.get(stageId);

            if (pipe != null) {
                movement.setPipeline(pipe);
            }
            if (deal != null) {
                movement.setBusinessDeal(deal);
            }
            if (stage != null) {
                movement.setStage(stage);
            }
            movementsToSave.add(movement);
        }
        movementRepository.saveAll(movementsToSave);
        System.out.println("Movimentações importados com sucesso!");
    }
}
