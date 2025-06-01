package br.com.pedrotfs.maestro.file.impl;

import br.com.pedrotfs.maestro.domain.Draw;
import br.com.pedrotfs.maestro.util.Constants;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {

    private final List<Draw> draws = new ArrayList<>();

    private String register;

    private long startFrom;

    public NoModelDataListener(String register, long startFrom) {
        this.register = register;
        this.startFrom = startFrom;
    }

    @Override
    public void invoke(Map<Integer, String> row, AnalysisContext context) {
        Draw d = new Draw();
        d.setRegisterId(register);
        d.set_id(row.get(0));
        if(Long.parseLong(d.get_id()) > this.startFrom) {
            if(register.equals(Constants.MGS)) {
                populateMgs(row, d);
            }
            this.draws.add(d);
        }
    }

    private void populateMgs(Map<Integer, String> row, Draw d) {
        if(row.get(9) == null) {
            d.setCity("");
        } else {
            d.setCity(row.get(9));
        }

        if(row.get(1) == null) {
            d.setDate("");
        } else {
            d.setDate(row.get(1));
        }

        if(row.get(19) == null) {
            d.setObs("");
        } else {
            d.setObs(row.get(19));
        }

        if(row.get(15) == null) {
            d.setAcumulado("R$ 0,00");
        } else {
            d.setAcumulado(row.get(15));
        }

        //bolas
        for(int i = 2; i <= 7; i++) {
            d.getNumbers().add(Integer.parseInt(row.get(i)));
        }

        //ganhadores - numero
        d.getWinnerCategoriesAmount().add(Integer.parseInt(row.get(8)));
        d.getWinnerCategoriesAmount().add(Integer.parseInt(row.get(11)));
        d.getWinnerCategoriesAmount().add(Integer.parseInt(row.get(13)));
        //ganhadores - valores
        d.getWinnerCategoriesDividends().add(Long.parseLong(row.get(10).replaceAll("[^\\d]", "")));
        d.getWinnerCategoriesDividends().add(Long.parseLong(row.get(12).replaceAll("[^\\d]", "")));
        d.getWinnerCategoriesDividends().add(Long.parseLong(row.get(14).replaceAll("[^\\d]", "")));

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("All rows processed.");
    }

    public List<Draw> getDraws() {
        return draws;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }
}
