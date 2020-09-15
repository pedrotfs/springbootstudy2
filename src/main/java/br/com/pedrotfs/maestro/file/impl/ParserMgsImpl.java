package br.com.pedrotfs.maestro.file.impl;

import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component("parserMgsImpl")
public class ParserMgsImpl extends ParserImpl {

    @Override
    protected boolean filterLines(Element e) {
        return e.toString().startsWith(getMatchingPatternHolder().getMgsEven()) || e.toString().startsWith(getMatchingPatternHolder().getMgsOdd());
    }

}
