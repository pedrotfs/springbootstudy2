package br.com.pedrotfs.maestro.service;

import br.com.pedrotfs.maestro.domain.Draw;
import br.com.pedrotfs.maestro.domain.Register;
import br.com.pedrotfs.maestro.util.ProbabilityDTO;

import java.util.List;

public interface DrawCalculationsService {

    Draw getHigherDividends(final String registerId);

    Draw getHigherAmount(final String registerId);

    int countDrawsByRegister(final String registerId);

    List<ProbabilityDTO> getListWithProbabilities(final Register register);
}
