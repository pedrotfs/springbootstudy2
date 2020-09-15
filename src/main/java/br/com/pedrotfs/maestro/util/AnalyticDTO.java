package br.com.pedrotfs.maestro.util;

import java.util.ArrayList;
import java.util.List;

public class AnalyticDTO {

    private List<Integer> intervals = new ArrayList<>();

    private List<Integer> companions = new ArrayList<>();

    private Double average;

    private Integer nextOuting;

    public List<Integer> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Integer> intervals) {
        this.intervals = intervals;
    }

    public List<Integer> getCompanions() {
        return companions;
    }

    public void setCompanions(List<Integer> companions) {
        this.companions = companions;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Integer getNextOuting() {
        return nextOuting;
    }

    public void setNextOuting(Integer nextOuting) {
        this.nextOuting = nextOuting;
    }
}
