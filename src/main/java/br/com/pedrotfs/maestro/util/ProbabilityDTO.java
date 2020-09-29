package br.com.pedrotfs.maestro.util;

public class ProbabilityDTO {

    private Integer number;

    private String chance;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getChance() {
        return chance;
    }

    public void setChance(String chance) {
        this.chance = chance;
    }

    public ProbabilityDTO(Integer number, String chance) {
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
