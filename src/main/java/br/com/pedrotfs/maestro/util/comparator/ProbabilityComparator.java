package br.com.pedrotfs.maestro.util.comparator;

import br.com.pedrotfs.maestro.util.ProbabilityDTO;

import java.util.Comparator;

public class ProbabilityComparator implements Comparator<ProbabilityDTO> {

    @Override
    public int compare(ProbabilityDTO a, ProbabilityDTO b) {
        if(a.getChance().equals(b.getChance())) {
            return 0;
        }
        else if(a.getChance() > b.getChance()) {
            return -1;
        } else {
            return 1;
        }
    }
}
