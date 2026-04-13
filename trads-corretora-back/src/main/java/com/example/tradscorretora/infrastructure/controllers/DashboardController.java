package com.example.tradscorretora.infrastructure.controllers;

import com.example.tradscorretora.domain.dto.*;
import com.example.tradscorretora.domain.entity.UserAcess;
import com.example.tradscorretora.infrastructure.services.DashboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping("/vw-gain")
    public Page<VwGainDTO> getVwGain(
            @PageableDefault(size = 5) Pageable pageable,
            @AuthenticationPrincipal UserAcess userAcess,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
            ) {
        return dashboardService.getVwWin(pageable, userAcess, userId, startDate, endDate);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping("/vw-loss")
    public Page<VwLossDTO> getVwLoss(
            @PageableDefault(size = 5) Pageable pageable,
            @AuthenticationPrincipal UserAcess userAcess,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return dashboardService.getVwLoss(pageable, userAcess, userId, startDate, endDate);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping("/vw-time-stage")
    public Page<VwTimeStageDTO> getVwTimeStage(
            @PageableDefault(size = 5) Pageable pageable,
            @AuthenticationPrincipal UserAcess userAcess,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
            ) {
        return dashboardService.getVwTimeStage(pageable, userAcess, userId, startDate, endDate);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping("/vw-transitions")
    public Page<VwTransitionsDTO> getVwTransitions(
            @PageableDefault(size = 5) Pageable pageable,
            @AuthenticationPrincipal UserAcess userAcess,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate

    ) {
        return dashboardService.getVwTransitions(pageable, userAcess, userId, startDate, endDate);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping("/vw-loss-conversion")
    public List<VwLossConversionDTO> getVwLossConversion(
            @AuthenticationPrincipal UserAcess userAcess,
            @RequestParam(required = false) Long userId
    ) {
        return dashboardService.getVwLossConversion(userAcess, userId);
    }
}
