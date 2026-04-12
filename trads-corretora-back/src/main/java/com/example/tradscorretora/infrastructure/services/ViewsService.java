package com.example.tradscorretora.infrastructure.services;

import com.example.tradscorretora.domain.entity.views.VwGain;
import com.example.tradscorretora.domain.entity.views.VwLoss;
import com.example.tradscorretora.domain.entity.views.VwTimeStage;
import com.example.tradscorretora.domain.entity.views.VwTransicoes;
import com.example.tradscorretora.infrastructure.repositories.VwGainRepository;
import com.example.tradscorretora.infrastructure.repositories.VwLossRepository;
import com.example.tradscorretora.infrastructure.repositories.VwTimeStageRepository;
import com.example.tradscorretora.infrastructure.repositories.VwTransicoesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewsService {

    private final VwLossRepository vwLossRepository;
    private final VwGainRepository vwWinRepository;
    private final VwTimeStageRepository vwTimeStageRepository;
    private final VwTransicoesRepository vwTransicoesRepository;

    public ViewsService(VwLossRepository vwLossRepository, VwGainRepository vwWinRepository, VwTimeStageRepository vwTimeStageRepository, VwTransicoesRepository vwTransicoesRepository) {
        this.vwLossRepository = vwLossRepository;
        this.vwWinRepository = vwWinRepository;
        this.vwTimeStageRepository = vwTimeStageRepository;
        this.vwTransicoesRepository = vwTransicoesRepository;
    }

    public List<VwLoss> getVwLossRepository() {
        return vwLossRepository.findAll();
    }

    public List<VwGain> getVwWinRepository() {
        return vwWinRepository.findAll();
    }

    public List<VwTimeStage> getVwTimeStageRepository() {
        return vwTimeStageRepository.findAll();
    }

    public List<VwTransicoes> getVwTransicoesRepository() {
        return vwTransicoesRepository.findAll();
    }
}
