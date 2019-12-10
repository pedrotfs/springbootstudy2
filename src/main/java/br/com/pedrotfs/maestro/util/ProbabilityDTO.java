package br.com.pedrotfs.maestro.util;

public class ProbabilityDTO {

    private Integer number;

    private Double chance;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getChance() {
        return chance;
    }

    public void setChance(Double chance) {
        this.chance = chance;
    }

    public ProbabilityDTO(Integer number, Double chance) {
        this.number = number;
        this.chance = chance;
    }

    @Override
    public String toString() {
        return "ProbabilityDTO{" +
                "number=" + number +
                ", chance=" + chance +
                '}';
    }
}
