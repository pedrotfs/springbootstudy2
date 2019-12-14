package br.com.pedrotfs.maestro.util.comparator;

import br.com.pedrotfs.maestro.util.ProbabilityDTO;

import java.util.Comparator;

public class ProbabilityNumberComparator implements Comparator<ProbabilityDTO> {

    @Override
    public int compare(ProbabilityDTO a, ProbabilityDTO b) {
        if(a.getNumber().equals(b.getNumber())) {
            return 0;
        }
        else if(a.getNumber() > b.getNumber()) {
            return -1;
        } else {
            return 1;
        }
    }
}
