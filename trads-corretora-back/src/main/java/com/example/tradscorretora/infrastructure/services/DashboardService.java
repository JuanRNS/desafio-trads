package com.example.tradscorretora.infrastructure.services;

import com.example.tradscorretora.domain.dto.*;
import com.example.tradscorretora.domain.entity.UserAcess;
import com.example.tradscorretora.domain.entity.views.VwLossConversion;
import com.example.tradscorretora.domain.entity.views.VwTransicoes;
import com.example.tradscorretora.domain.enums.RoleEnum;
import com.example.tradscorretora.infrastructure.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;


@Service
public class DashboardService {

    private final VwLossRepository vwLossRepository;
    private final VwGainRepository vwWinRepository;
    private final VwTimeStageRepository vwTimeStageRepository;
    private final VwTransicoesRepository vwTransicoesRepository;
    private final VwLossConversionRepository vwLossConversionRepository;

    public DashboardService(VwLossRepository vwLossRepository, VwGainRepository vwWinRepository, VwTimeStageRepository vwTimeStageRepository, VwTransicoesRepository vwTransicoesRepository, VwLossConversionRepository vwLossConversionRepository) {
        this.vwLossRepository = vwLossRepository;
        this.vwWinRepository = vwWinRepository;
        this.vwTimeStageRepository = vwTimeStageRepository;
        this.vwTransicoesRepository = vwTransicoesRepository;
        this.vwLossConversionRepository = vwLossConversionRepository;
    }

    public Page<VwLossDTO> getVwLoss(Pageable pageable, UserAcess userAcess, Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;
        return getGenericData(pageable, userAcess, userId, startDateTime, endDateTime,
                 (id, s, e, p) -> vwLossRepository.findByUserIdAndDate(id, p, s, e),
                (s, e, p) -> vwLossRepository.findAllByDate(p, s, e),
                loss -> new VwLossDTO(loss.getId(), loss.getIdNegocio(), loss.getPipelineId(), loss.getLossDate(), loss.getOpportunity(), loss.getCurrencyId())
        );
    }

    public Page<VwGainDTO> getVwWin(Pageable pageable, UserAcess userAcess, Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;
        return getGenericData(pageable, userAcess, userId, startDateTime, endDateTime,
                (id, s, e, p) -> vwWinRepository.findByUserIdAndDate(id, p, s, e),
                (s, e, p) -> vwWinRepository.findAllByDate(p, s, e),
                gain -> new VwGainDTO(gain.getId(), gain.getIdNegocio(), gain.getPipelineId(), gain.getWinDate(), gain.getOpportunity(), gain.getCurrencyId())
        );
    }

    public Page<VwTimeStageDTO> getVwTimeStage(Pageable pageable, UserAcess userAcess, Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;
        return getGenericData(pageable, userAcess, userId, startDateTime, endDateTime,
                 (id, s, e, p) -> vwTimeStageRepository.findByUserIdByDate(id, p, s, e),
                (s, e, p) -> vwTimeStageRepository.findAllByDate(p, s, e),
                stage -> new VwTimeStageDTO(stage.getId(), stage.getIdNegocio(), stage.getPipelineId(), stage.getStageId(), stage.getStart(), stage.getEnd(), stage.getDaysInStage())
        );
    }

    public Page<VwTransitionsDTO> getVwTransitions(Pageable pageable, UserAcess userAcess, Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;
        return getGenericData(pageable, userAcess, userId, startDateTime, endDateTime,
                (id, s,e , p) -> vwTransicoesRepository.findByUserIdAndStartBetween(id, s, e, p),
                (s, e, p) -> vwTransicoesRepository.findAllByStartBetween(p,s, e),
                (VwTransicoes t) -> new VwTransitionsDTO(t.getId(), t.getIdNegocio(), t.getPipelineId(), t.getStageFrom(), t.getStageTo(), t.getTransicao(), t.getSortTo(), t.getSortFrom())
        );
    }

    public List<VwLossConversionDTO> getVwLossConversion(UserAcess userAcess, Long userId) {
        List<VwLossConversion> vwLossConversionList = this.vwLossConversionRepository.findAll();
        if (userAcess.getRole() == RoleEnum.USER) {
            vwLossConversionList = this.vwLossConversionRepository.findByUserId(userAcess.getId());
            return vwLossConversionList
                    .stream()
                    .map(lc -> new VwLossConversionDTO(lc.getStatusId(), lc.getStageFrom(), lc.getTotalTransitions()))
                    .toList();
        } else if (userId != null) {
            vwLossConversionList = this.vwLossConversionRepository.findByUserId(userId);
            return vwLossConversionList
                    .stream()
                    .map(lc -> new VwLossConversionDTO(lc.getStatusId(), lc.getStageFrom(), lc.getTotalTransitions()))
                    .toList();
        }
        return vwLossConversionList
                .stream()
                .map(lc -> new VwLossConversionDTO(lc.getStatusId(), lc.getStageFrom(), lc.getTotalTransitions()))
                .toList();
    }

    private <T, R> Page<R> getGenericData(
            Pageable pageable,
            UserAcess userAcess,
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            DashboardProvider<T> findByFiltersFn,
            DashboardDateProvider<T> findAllByDateFn,
            Function<T, R> mapper
    ) {
        Page<T> data;
        if (userAcess.getRole() == RoleEnum.USER) {
            data = findByFiltersFn.apply(userAcess.getId(), startDate, endDate, pageable);
        } else if (userId != null) {
            data = findByFiltersFn.apply(userId,startDate, endDate, pageable);
        } else {
            data = findAllByDateFn.apply(startDate, endDate, pageable);
        }
        return data.map(mapper);
    }

    @FunctionalInterface
    public interface DashboardProvider<T> {
        Page<T> apply(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    }

    @FunctionalInterface
    public interface DashboardDateProvider<T> {
        Page<T> apply(LocalDateTime start, LocalDateTime end, Pageable pageable);
    }
}
