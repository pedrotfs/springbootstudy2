package br.com.pedrotfs.maestro.file.impl;

import br.com.pedrotfs.maestro.domain.Draw;
import br.com.pedrotfs.maestro.file.Parser;
import com.alibaba.excel.EasyExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParserImpl implements Parser {

    private static Logger LOG = LoggerFactory.getLogger(ParserImpl.class);

    @Override
    public List<Draw> parse(final String originFileName, final String register, long startFrom) {
        LOG.info("parsing generated" + originFileName);
        NoModelDataListener readListener = new NoModelDataListener(register, startFrom);
        try {
            EasyExcel.read(originFileName, null, readListener).sheet().doRead();
        } catch(Exception e) {
            System.out.println("deu ruim");
        }
        LOG.info("parsed " + originFileName + ". result count: " + readListener.getDraws().size());
        return readListener.getDraws();
    }
}
