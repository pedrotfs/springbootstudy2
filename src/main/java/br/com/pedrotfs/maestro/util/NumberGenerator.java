package br.com.pedrotfs.maestro.util;

import br.com.pedrotfs.maestro.domain.Register;
import br.com.pedrotfs.maestro.util.comparator.ProbabilityNumberComparator;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
public class NumberGenerator {

    public List<Integer> generateNumbers(final Register register)
    {
        List<Integer> numbers = new ArrayList<>();
        for(int i = 1; i <= register.getCount(); i++) {
            numbers.add(i);
        }
        return numbers;
    }

    public List<Integer> treatSelectedNumberRemoval(final String buttonId, List<Integer> selectedNumbers) {
        if(selectedNumbers.size() > 1) {
            selectedNumbers.remove((Object) Integer.parseInt(buttonId));
        }
        else {
            selectedNumbers = new ArrayList<>();
        }
        return selectedNumbers;
    }

    public BigInteger getPossibilities(final Register register)
    {
        BigInteger result;
        BigInteger nFat = BigInteger.valueOf(1);
        BigInteger pFat = BigInteger.valueOf(1);
        BigInteger npFat = BigInteger.valueOf(1);
        for(int i = 1; i <= register.getCount(); i++) {
            nFat = nFat.multiply(BigInteger.valueOf(i));
        }
        for(int i = 1; i <= register.getLimit(); i++) {
            pFat = pFat.multiply(BigInteger.valueOf(i));
        }
        for(int i = 1; i <= register.getCount() - register.getLimit(); i++) {
            npFat = npFat.multiply(BigInteger.valueOf(i));
        }
        result = (nFat).divide(pFat.multiply(npFat));
        return result;
    }

    public List<ProbabilityDTO> getCommonAdvice(final List<ProbabilityDTO> dtos, final Register register, final boolean lesser) {
        List<ProbabilityDTO> list = new ArrayList<>();
        if(dtos.isEmpty()) {
            return list;
        }
        if(lesser) {
            list = new ArrayList<>(dtos.subList(register.getCount() - register.getLimit(), register.getCount()));
        } else {
            list = new ArrayList<>(dtos.subList(0, register.getLimit()));
        }
        list.sort(new ProbabilityNumberComparator());
        return list;
    }
}
