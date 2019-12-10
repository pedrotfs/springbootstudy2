package br.com.pedrotfs.maestro.util.comparator;

import br.com.pedrotfs.maestro.domain.Draw;

import java.util.Comparator;

public class DrawAmountComparator implements Comparator<Draw> {

    @Override
    public int compare(Draw a, Draw b) {
        if(a.getWinnerCategoriesAmount().get(0).equals(b.getWinnerCategoriesAmount().get(0))) {
            return 0;
        }
        else if(a.getWinnerCategoriesAmount().get(0) > b.getWinnerCategoriesAmount().get(0)) {
            return -1;
        } else {
            return 1;
        }
    }
}
