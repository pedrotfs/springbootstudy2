package br.com.pedrotfs.maestro.file.impl;

import br.com.pedrotfs.maestro.file.Parser;
import br.com.pedrotfs.maestro.util.MatchingPatternHolder;
import com.alibaba.excel.EasyExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParserImpl implements Parser {

    private static Logger LOG = LoggerFactory.getLogger(ParserImpl.class);

    @Autowired
    private MatchingPatternHolder matchingPatternHolder;

    @Override
    public List<String> parse(final String originFileName) {
        LOG.info("parsing generated" + originFileName);
        NoModelDataListener readListener = new NoModelDataListener(originFileName);
        try {
            EasyExcel.read(originFileName, null, readListener).sheet().doRead();
        } catch(Exception e) {
            System.out.println("deu ruim");
        }
        LOG.info("parsed " + originFileName + ". result count: " + readListener.getParsedList().size());
        return readListener.getParsedList();
    }

    public MatchingPatternHolder getMatchingPatternHolder() {
        return matchingPatternHolder;
    }
}
