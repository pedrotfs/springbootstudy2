package br.com.pedrotfs.maestro.util;

import br.com.pedrotfs.maestro.domain.Draw;

import java.util.List;

import static br.com.pedrotfs.maestro.util.Constants.MAX;

public class CheckDrawsMaxHelper {

    public static List<Draw> treatCheckDrawsToMax(List<Draw> lastCheckedDraws) {
        if(lastCheckedDraws.size() > MAX) {
            return lastCheckedDraws.subList(0,MAX);
        }
        return lastCheckedDraws;
    }
}
