package br.com.pedrotfs.maestro.util.comparator;

import br.com.pedrotfs.maestro.domain.Draw;

import java.util.Comparator;

public class DrawDivivdendComparator implements Comparator<Draw> {

    @Override
    public int compare(Draw a, Draw b) {
        if(a.getWinnerCategoriesDividends().get(0).equals(b.getWinnerCategoriesDividends().get(0))) {
            return 0;
        }
        else if(a.getWinnerCategoriesDividends().get(0) > b.getWinnerCategoriesDividends().get(0)) {
            return -1;
        } else {
            return 1;
        }
    }
}
