package br.com.pedrotfs.maestro.service.impl;

import br.com.pedrotfs.maestro.domain.Draw;
import br.com.pedrotfs.maestro.domain.Register;
import br.com.pedrotfs.maestro.repository.DrawRepository;
import br.com.pedrotfs.maestro.service.DrawCalculationsService;
import br.com.pedrotfs.maestro.util.ProbabilityDTO;
import br.com.pedrotfs.maestro.util.comparator.DrawDivivdendComparator;
import br.com.pedrotfs.maestro.util.comparator.ProbabilityComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DrawCalculationsServiceImpl implements DrawCalculationsService {

    @Autowired
    private DrawRepository drawRepository;

    @Override
    public Draw getHigherDividends(String registerId) {
        final Optional<Draw> higherDividend = drawRepository.findByRegisterId(registerId).stream().sorted(new DrawDivivdendComparator()).findFirst();
        return higherDividend.orElse(new Draw());
    }

    @Override
    public Draw getHigherAmount(String registerId) {
        final Optional<Draw> higherDividend = drawRepository.findByRegisterId(registerId).stream().sorted(new DrawDivivdendComparator()).findFirst();
        return higherDividend.orElse(new Draw());
    }

    @Override
    public int countDrawsByRegister(String registerId) {
        return drawRepository.findByRegisterId(registerId).size();
    }

    @Override
    public List<ProbabilityDTO> getListWithProbabilities(Register register) {
        List<ProbabilityDTO> dtos = new ArrayList<>();
        List<Draw> draws = drawRepository.findByRegisterId(register.get_id());
        if(draws == null || draws.isEmpty()) {
            return dtos;
        }
        double chance = 0D;
        for(Integer i = 1; i <= register.getCount(); i++) {
            final Integer finalInteger = i;
            chance = (double) draws.stream().filter(d -> d.getNumbers().contains(finalInteger)).count() / draws.size();
            ProbabilityDTO dto = new ProbabilityDTO(finalInteger, new BigDecimal(chance).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
            dtos.add(dto);
        }
        dtos.sort(new ProbabilityComparator());
        return dtos;
    }

}
