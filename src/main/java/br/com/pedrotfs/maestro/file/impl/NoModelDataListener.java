package br.com.pedrotfs.maestro.file.impl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {

    private static final String SEPARATOR = "-";

    private static final String PLACEHOLDER = "placeholder";

    private List<String> parsedList = new ArrayList<>();

    private String register;

    public NoModelDataListener(String register) {
        this.register = register;
    }

    @Override
    public void invoke(Map<Integer, String> row, AnalysisContext context) {
        row.get(0);

        StringBuilder parsedLine = new StringBuilder();
        parsedLine.append(row.get(0));
        mgs(row, parsedLine);
        parsedList.add(parsedLine.toString());
    }

    private void mgs(Map<Integer, String> row, StringBuilder sb) {
        //origem : id - data - bolas - ganhadores 6 - cidade - rateio $ - ganhadores 5 - $ - ganahdores 4 - $ - ... - acumulado - obs
        //destino: id - bolas - ganhadores - premios - data
        //bolas
        sb.append(SEPARATOR);
        sb.append(row.get(2));
        sb.append(SEPARATOR);
        sb.append(row.get(3));
        sb.append(SEPARATOR);
        sb.append(row.get(4));
        sb.append(SEPARATOR);
        sb.append(row.get(5));
        sb.append(SEPARATOR);
        sb.append(row.get(6));
        sb.append(SEPARATOR);
        sb.append(row.get(7));
        //ganhadores? -
        sb.append(SEPARATOR);
        sb.append(row.get(8));
        sb.append(SEPARATOR);
        sb.append(row.get(10));
        sb.append(SEPARATOR);
        sb.append(row.get(11));
        sb.append(SEPARATOR);
        sb.append(row.get(12));
        sb.append(SEPARATOR);
        sb.append(row.get(13));
        sb.append(SEPARATOR);
        sb.append(row.get(14));
        //acumulado
        sb.append(SEPARATOR);
        sb.append(row.get(15));

        //datas
        sb.append(SEPARATOR);
        sb.append(row.get(1));

        //cidade
        sb.append(SEPARATOR);
        sb.append(row.get(9));
        //obs
        sb.append(SEPARATOR);
        sb.append(row.get(19));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("All rows processed.");
    }


    public List<String> getParsedList() {
        return parsedList;
    }
}
