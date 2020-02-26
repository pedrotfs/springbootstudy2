package br.com.pedrotfs.maestro.util.comparator;

import br.com.pedrotfs.maestro.domain.Draw;

import java.util.Comparator;

public class DrawIdComparator  implements Comparator<Draw> {

    @Override
    public int compare(Draw a, Draw b) {
        if(a.get_id().equals(b.get_id())) {
            return 0;
        }
        else if(Integer.parseInt(a.get_id().substring(3)) < Integer.parseInt(b.get_id().substring(3))) {
            return -1;
        } else {
            return 1;
        }
    }
}
