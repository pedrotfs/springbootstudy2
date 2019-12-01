package br.com.pedrotfs.maestro.domain;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class Draw {

    @Id
    private String _id;

    private String registerId;

    private List<Integer> numbers = new ArrayList<>();

    private List<Long> winnerCategoriesDividends = new ArrayList<>();

    private List<Integer> winnerCategoriesAmount = new ArrayList<>();

    private String date;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }

    public List<Long> getWinnerCategoriesDividends() {
        return winnerCategoriesDividends;
    }

    public void setWinnerCategoriesDividends(List<Long> winnerCategoriesDividends) {
        this.winnerCategoriesDividends = winnerCategoriesDividends;
    }

    public List<Integer> getWinnerCategoriesAmount() {
        return winnerCategoriesAmount;
    }

    public void setWinnerCategoriesAmount(List<Integer> winnerCategoriesAmount) {
        this.winnerCategoriesAmount = winnerCategoriesAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
