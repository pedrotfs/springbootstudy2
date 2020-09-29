package br.com.pedrotfs.maestro.util.comparator;

import br.com.pedrotfs.maestro.util.ProbabilityDTO;

import java.math.BigDecimal;
import java.util.Comparator;

public class ProbabilityComparator implements Comparator<ProbabilityDTO> {

    @Override
    public int compare(ProbabilityDTO a, ProbabilityDTO b) {

        BigDecimal aNumber = new BigDecimal(a.getChance());
        BigDecimal bNumber = new BigDecimal(b.getChance());

        if(aNumber.equals(bNumber)) {
            return 0;
        }
        else if(aNumber.compareTo(bNumber) > 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
