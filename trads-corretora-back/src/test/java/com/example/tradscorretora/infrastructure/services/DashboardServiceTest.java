//package com.example.tradscorretora.infrastructure.services;
//
//import com.example.tradscorretora.domain.dto.*;
//import com.example.tradscorretora.domain.entity.UserAcess;
//import com.example.tradscorretora.domain.entity.views.*;
//import com.example.tradscorretora.domain.enums.RoleEnum;
//import com.example.tradscorretora.infrastructure.repositories.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//class DashboardServiceTest {
//
//    @Mock
//    private VwLossRepository vwLossRepository;
//    @Mock
//    private VwGainRepository vwGainRepository;
//    @Mock
//    private VwTimeStageRepository vwTimeStageRepository;
//    @Mock
//    private VwTransicoesRepository vwTransicoesRepository;
//    @Mock
//    private VwLossConversionRepository vwLossConversionRepository;
//
//    private DashboardService dashboardService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        dashboardService = new DashboardService(
//                vwLossRepository,
//                vwGainRepository,
//                vwTimeStageRepository,
//                vwTransicoesRepository,
//                vwLossConversionRepository
//        );
//    }
//
//    @Test
//    void getVwLoss_usesUserRoleUserId() {
//        Pageable pageable = PageRequest.of(0, 10);
//        UserAcess userAcess = user(RoleEnum.USER, 10L);
//        VwLoss loss = mock(VwLoss.class);
//        when(loss.getId()).thenReturn(1L);
//        when(loss.getIdNegocio()).thenReturn(2L);
//        when(loss.getPipelineId()).thenReturn(3L);
//        Timestamp lossDate = Timestamp.valueOf("2025-01-01 10:00:00");
//        when(loss.getLossDate()).thenReturn(lossDate);
//        when(loss.getOpportunity()).thenReturn(new BigDecimal("123.45"));
//        when(loss.getCurrencyId()).thenReturn("USD");
//        when(loss.getUserId()).thenReturn(10L);
//        Page<VwLoss> page = new PageImpl<>(List.of(loss), pageable, 1);
//        when(vwLossRepository.findByUserId(10L, pageable)).thenReturn(page);
//
//        Page<VwLossDTO> result = dashboardService.getVwLoss(pageable, userAcess, null);
//
//        assertThat(result.getContent()).hasSize(1);
//        VwLossDTO dto = result.getContent().get(0);
//        assertThat(dto.id()).isEqualTo(1L);
//        assertThat(dto.idNegocio()).isEqualTo(2L);
//        assertThat(dto.pipelineId()).isEqualTo(3L);
//        assertThat(dto.lossDate()).isEqualTo(lossDate);
//        assertThat(dto.opportunity()).isEqualByComparingTo("123.45");
//        assertThat(dto.currencyId()).isEqualTo("USD");
//        assertThat(dto.userId()).isEqualTo(10L);
//        verify(vwLossRepository).findByUserId(10L, pageable);
//        verify(vwLossRepository, never()).findAll(any(Pageable.class));
//    }
//
//    @Test
//    void getVwLoss_usesProvidedUserIdWhenNotUserRole() {
//        Pageable pageable = PageRequest.of(0, 10);
//        UserAcess userAcess = user(RoleEnum.ADMIN, 10L);
//        Page<VwLoss> page = new PageImpl<>(List.of(mock(VwLoss.class)), pageable, 1);
//        when(vwLossRepository.findByUserId(99L, pageable)).thenReturn(page);
//
//        dashboardService.getVwLoss(pageable, userAcess, 99L);
//
//        verify(vwLossRepository).findByUserId(99L, pageable);
//        verify(vwLossRepository, never()).findAll(any(Pageable.class));
//    }
//
//    @Test
//    void getVwLoss_usesFindAllWhenNoUserIdAndNotUserRole() {
//        Pageable pageable = PageRequest.of(0, 10);
//        UserAcess userAcess = user(RoleEnum.MANAGER, 10L);
//        Page<VwLoss> page = new PageImpl<>(List.of(mock(VwLoss.class)), pageable, 1);
//        when(vwLossRepository.findAll(pageable)).thenReturn(page);
//
//        dashboardService.getVwLoss(pageable, userAcess, null);
//
//        verify(vwLossRepository).findAll(pageable);
//        verify(vwLossRepository, never()).findByUserId(anyLong(), any(Pageable.class));
//    }
//
//    @Test
//    void getVwWin_mapsAndUsesUserRoleUserId() {
//        Pageable pageable = PageRequest.of(0, 10);
//        UserAcess userAcess = user(RoleEnum.USER, 7L);
//        VwGain gain = mock(VwGain.class);
//        when(gain.getId()).thenReturn(5L);
//        when(gain.getIdNegocio()).thenReturn(6L);
//        when(gain.getPipelineId()).thenReturn(7L);
//        Timestamp winDate = Timestamp.valueOf("2025-02-02 08:30:00");
//        when(gain.getWinDate()).thenReturn(winDate);
//        when(gain.getOpportunity()).thenReturn(new BigDecimal("999.00"));
//        when(gain.getCurrencyId()).thenReturn("EUR");
//        when(gain.getUserId()).thenReturn(7L);
//        Page<VwGain> page = new PageImpl<>(List.of(gain), pageable, 1);
//        when(vwGainRepository.findByUserId(7L, pageable)).thenReturn(page);
//
//        Page<VwGainDTO> result = dashboardService.getVwWin(pageable, userAcess, null);
//
//        assertThat(result.getContent()).hasSize(1);
//        VwGainDTO dto = result.getContent().get(0);
//        assertThat(dto.id()).isEqualTo(5L);
//        assertThat(dto.idNegocio()).isEqualTo(6L);
//        assertThat(dto.pipelineId()).isEqualTo(7L);
//        assertThat(dto.winDate()).isEqualTo(winDate);
//        assertThat(dto.opportunity()).isEqualByComparingTo("999.00");
//        assertThat(dto.currencyId()).isEqualTo("EUR");
//        assertThat(dto.userId()).isEqualTo(7L);
//        verify(vwGainRepository).findByUserId(7L, pageable);
//    }
//
//    @Test
//    void getVwTimeStage_mapsAndUsesUserRoleUserId() {
//        Pageable pageable = PageRequest.of(0, 10);
//        UserAcess userAcess = user(RoleEnum.USER, 3L);
//        VwTimeStage stage = mock(VwTimeStage.class);
//        when(stage.getId()).thenReturn(11L);
//        when(stage.getIdNegocio()).thenReturn(12L);
//        when(stage.getPipelineId()).thenReturn(13L);
//        when(stage.getStageId()).thenReturn("stage-1");
//        Timestamp start = Timestamp.valueOf("2025-03-03 09:00:00");
//        Timestamp end = Timestamp.valueOf("2025-03-05 18:00:00");
//        when(stage.getStart()).thenReturn(start);
//        when(stage.getEnd()).thenReturn(end);
//        when(stage.getDaysInStage()).thenReturn(2L);
//        when(stage.getUserId()).thenReturn(3L);
//        Page<VwTimeStage> page = new PageImpl<>(List.of(stage), pageable, 1);
//        when(vwTimeStageRepository.findByUserId(3L, pageable)).thenReturn(page);
//
//        Page<VwTimeStageDTO> result = dashboardService.getVwTimeStage(pageable, userAcess, null);
//
//        assertThat(result.getContent()).hasSize(1);
//        VwTimeStageDTO dto = result.getContent().get(0);
//        assertThat(dto.id()).isEqualTo(11L);
//        assertThat(dto.idNegocio()).isEqualTo(12L);
//        assertThat(dto.pipelineId()).isEqualTo(13L);
//        assertThat(dto.stageId()).isEqualTo("stage-1");
//        assertThat(dto.start()).isEqualTo(start);
//        assertThat(dto.end()).isEqualTo(end);
//        assertThat(dto.daysInStage()).isEqualTo(2L);
//        assertThat(dto.userId()).isEqualTo(3L);
//        verify(vwTimeStageRepository).findByUserId(3L, pageable);
//    }
//
//    @Test
//    void getVwTransitions_mapsAndUsesUserRoleUserId() {
//        Pageable pageable = PageRequest.of(0, 10);
//        UserAcess userAcess = user(RoleEnum.USER, 8L);
//        VwTransicoes transicoes = mock(VwTransicoes.class);
//        when(transicoes.getId()).thenReturn(21L);
//        when(transicoes.getIdNegocio()).thenReturn(22L);
//        when(transicoes.getPipelineId()).thenReturn(23L);
//        when(transicoes.getStageFrom()).thenReturn("from");
//        when(transicoes.getStageTo()).thenReturn("to");
//        Timestamp transition = Timestamp.valueOf("2025-04-04 11:11:11");
//        when(transicoes.getTransicao()).thenReturn(transition);
//        when(transicoes.getSortTo()).thenReturn(1);
//        when(transicoes.getSortFrom()).thenReturn(2);
//        Page<VwTransicoes> page = new PageImpl<>(List.of(transicoes), pageable, 1);
//        when(vwTransicoesRepository.findByUserId(8L, pageable)).thenReturn(page);
//
//        Page<VwTransitionsDTO> result = dashboardService.getVwTransitions(pageable, userAcess, null);
//
//        assertThat(result.getContent()).hasSize(1);
//        VwTransitionsDTO dto = result.getContent().get(0);
//        assertThat(dto.id()).isEqualTo(21L);
//        assertThat(dto.idNegocio()).isEqualTo(22L);
//        assertThat(dto.pipelineId()).isEqualTo(23L);
//        assertThat(dto.stageFrom()).isEqualTo("from");
//        assertThat(dto.stageTo()).isEqualTo("to");
//        assertThat(dto.transition()).isEqualTo(transition);
//        assertThat(dto.sortTo()).isEqualTo(1);
//        assertThat(dto.sortFrom()).isEqualTo(2);
//        verify(vwTransicoesRepository).findByUserId(8L, pageable);
//    }
//
//    @Test
//    void getVwLossConversion_usesUserRoleUserId() {
//        UserAcess userAcess = user(RoleEnum.USER, 4L);
//        VwLossConversion conversion = mock(VwLossConversion.class);
//        when(conversion.getStatusId()).thenReturn("lost");
//        when(conversion.getStageFrom()).thenReturn("stage-2");
//        when(conversion.getTotalTransitions()).thenReturn(15L);
//        when(vwLossConversionRepository.findByUserId(4L)).thenReturn(List.of(conversion));
//
//        List<VwLossConversionDTO> result = dashboardService.getVwLossConversion(userAcess, null);
//
//        assertThat(result).hasSize(1);
//        VwLossConversionDTO dto = result.get(0);
//        assertThat(dto.statusId()).isEqualTo("lost");
//        assertThat(dto.stageFrom()).isEqualTo("stage-2");
//        assertThat(dto.count()).isEqualTo(15L);
//        verify(vwLossConversionRepository).findByUserId(4L);
//        verify(vwLossConversionRepository).findAll();
//    }
//
//    @Test
//    void getVwLossConversion_usesProvidedUserIdWhenNotUserRole() {
//        UserAcess userAcess = user(RoleEnum.ADMIN, 4L);
//        VwLossConversion conversion = mock(VwLossConversion.class);
//        when(conversion.getStatusId()).thenReturn("lost");
//        when(conversion.getStageFrom()).thenReturn("stage-3");
//        when(conversion.getTotalTransitions()).thenReturn(20L);
//        when(vwLossConversionRepository.findByUserId(9L)).thenReturn(List.of(conversion));
//
//        List<VwLossConversionDTO> result = dashboardService.getVwLossConversion(userAcess, 9L);
//
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).count()).isEqualTo(20L);
//        verify(vwLossConversionRepository).findByUserId(9L);
//        verify(vwLossConversionRepository).findAll();
//    }
//
//    @Test
//    void getVwLossConversion_usesFindAllWhenNoUserIdAndNotUserRole() {
//        UserAcess userAcess = user(RoleEnum.MANAGER, 4L);
//        VwLossConversion conversion = mock(VwLossConversion.class);
//        when(conversion.getStatusId()).thenReturn("won");
//        when(conversion.getStageFrom()).thenReturn("stage-4");
//        when(conversion.getTotalTransitions()).thenReturn(25L);
//        when(vwLossConversionRepository.findAll()).thenReturn(List.of(conversion));
//
//        List<VwLossConversionDTO> result = dashboardService.getVwLossConversion(userAcess, null);
//
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).stageFrom()).isEqualTo("stage-4");
//        verify(vwLossConversionRepository).findAll();
//        verify(vwLossConversionRepository, never()).findByUserId(anyLong());
//    }
//
//    private UserAcess user(RoleEnum role, Long id) {
//        UserAcess userAcess = new UserAcess();
//        userAcess.setId(id);
//        userAcess.setRole(role);
//        return userAcess;
//    }
//}
