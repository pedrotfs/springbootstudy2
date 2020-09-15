package br.com.pedrotfs.maestro.assembler.impl;

import br.com.pedrotfs.maestro.assembler.Assembler;
import br.com.pedrotfs.maestro.domain.Draw;
import br.com.pedrotfs.maestro.util.MatchingPatternHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AssemblerImpl implements Assembler {

    @Autowired
    private MatchingPatternHolder matchingPatternHolder;

    private static Logger LOG = LoggerFactory.getLogger(AssemblerImpl.class);

    @Override
    public List<Draw> assemble(List<String> list) {
        LOG.info("Assembling parsed objects");
        List<Draw> results = new ArrayList<>();
        list.forEach(l -> convertLine(results, l));
        LOG.info("assembly finished succesfully. " + results.size() + " items inbound for transfer.");
        return results;
    }

    @Override
    public List<Draw> assembleMgs(List<String> list) {
        LOG.info("Assembling parsed objects");
        List<Draw> results = new ArrayList<>();
        list.forEach(l -> convertLineMgs(results, l));
        LOG.info("assembly finished succesfully. " + results.size() + " items inbound for transfer.");
        return results;
    }

    private void convertLine(List<Draw> result, String line) {
        final String[] split = line.split(matchingPatternHolder.getSplit());
        Draw draw = new Draw();

        draw.set_id("ltf" + split[1]);
        draw.setRegisterId("ltf");
        populateNumbers(split, draw);
        populateCategoriesAmount(split, draw);
        populateCategoriesDividends(split, draw);

        Collections.sort(draw.getNumbers());
        Collections.sort(draw.getWinnerCategoriesDividends());
        Collections.sort(draw.getWinnerCategoriesAmount());
        Collections.reverse(draw.getWinnerCategoriesDividends());

        draw.setDate(split[2]);
        result.add(draw);
    }

    private void convertLineMgs(List<Draw> result, String line) {
        final String[] split = line.split(matchingPatternHolder.getSplit());
        Draw draw = new Draw();

        draw.set_id("mgs" + split[1]);
        draw.setRegisterId("mgs");
        populateNumbersMgs(split, draw);
        populateCategoriesAmountMgs(split, draw);
        populateCategoriesDividendsMgs(split, draw);

        Collections.sort(draw.getNumbers());
        Collections.sort(draw.getWinnerCategoriesDividends());
        Collections.sort(draw.getWinnerCategoriesAmount());
        Collections.reverse(draw.getWinnerCategoriesDividends());

        draw.setDate(split[2]);
        result.add(draw);
    }

    private void populateCategoriesDividends(String[] split, Draw d) {
        d.getWinnerCategoriesDividends().add(Long.parseLong(split[24].replace(".", "").replace(",", "")));
        d.getWinnerCategoriesDividends().add(Long.parseLong(split[25].replace(".", "").replace(",", "")));
        d.getWinnerCategoriesDividends().add(Long.parseLong(split[26].replace(".", "").replace(",", "")));
        d.getWinnerCategoriesDividends().add(Long.parseLong(split[27].replace(".", "").replace(",", "")));
        d.getWinnerCategoriesDividends().add(Long.parseLong(split[28].replace(".", "").replace(",", "")));
    }

    private void populateCategoriesDividendsMgs(String[] split, Draw draw) {
        draw.getWinnerCategoriesDividends().add(Long.parseLong(split[11].replace(".", "").replace(",", "")));
        draw.getWinnerCategoriesDividends().add(Long.parseLong(split[13].replace(".", "").replace(",", "")));
        draw.getWinnerCategoriesDividends().add(Long.parseLong(split[15].replace(".", "").replace(",", "")));
        draw.getWinnerCategoriesDividends().add(Long.parseLong(split[17].replace(".", "").replace(",", "")));
    }

    private void populateCategoriesAmount(String[] split, Draw draw) {
        draw.getWinnerCategoriesAmount().add(Integer.parseInt(split[19].replace(".", "").replace(",", "")));
        draw.getWinnerCategoriesAmount().add(Integer.parseInt(split[20].replace(".", "").replace(",", "")));
        draw.getWinnerCategoriesAmount().add(Integer.parseInt(split[21].replace(".", "").replace(",", "")));
        draw.getWinnerCategoriesAmount().add(Integer.parseInt(split[22].replace(".", "").replace(",", "")));
        draw.getWinnerCategoriesAmount().add(Integer.parseInt(split[23].replace(".", "").replace(",", "")));
    }

    private void populateCategoriesAmountMgs(String[] split, Draw draw) {
        draw.getWinnerCategoriesAmount().add(Integer.parseInt(split[10].replace(".", "").replace(",", "")));
        draw.getWinnerCategoriesAmount().add(Integer.parseInt(split[12].replace(".", "").replace(",", "")));
        draw.getWinnerCategoriesAmount().add(Integer.parseInt(split[14].replace(".", "").replace(",", "")));
    }

    private void populateNumbers(String[] split, Draw draw) {
        draw.getNumbers().add(Integer.parseInt(split[3]));
        draw.getNumbers().add(Integer.parseInt(split[4]));
        draw.getNumbers().add(Integer.parseInt(split[5]));
        draw.getNumbers().add(Integer.parseInt(split[6]));
        draw.getNumbers().add(Integer.parseInt(split[7]));
        draw.getNumbers().add(Integer.parseInt(split[8]));
        draw.getNumbers().add(Integer.parseInt(split[9]));
        draw.getNumbers().add(Integer.parseInt(split[10]));
        draw.getNumbers().add(Integer.parseInt(split[11]));
        draw.getNumbers().add(Integer.parseInt(split[12]));
        draw.getNumbers().add(Integer.parseInt(split[13]));
        draw.getNumbers().add(Integer.parseInt(split[14]));
        draw.getNumbers().add(Integer.parseInt(split[15]));
        draw.getNumbers().add(Integer.parseInt(split[16]));
        draw.getNumbers().add(Integer.parseInt(split[17]));
    }

    private void populateNumbersMgs(String[] split, Draw draw) {
        draw.getNumbers().add(Integer.parseInt(split[3]));
        draw.getNumbers().add(Integer.parseInt(split[4]));
        draw.getNumbers().add(Integer.parseInt(split[5]));
        draw.getNumbers().add(Integer.parseInt(split[6]));
        draw.getNumbers().add(Integer.parseInt(split[7]));
        draw.getNumbers().add(Integer.parseInt(split[8]));
    }
}
