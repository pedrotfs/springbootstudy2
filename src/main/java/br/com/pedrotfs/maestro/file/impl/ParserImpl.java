package br.com.pedrotfs.maestro.file.impl;

import br.com.pedrotfs.maestro.file.Parser;
import br.com.pedrotfs.maestro.util.MatchingPatternHolder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ParserImpl implements Parser {

    private static Logger LOG = LoggerFactory.getLogger(ParserImpl.class);

    @Autowired
    private MatchingPatternHolder matchingPatternHolder;

    @Override
    public List<String> parse(final String originFileName) {
        LOG.info("parsing generated" + originFileName);
        File file = new File(originFileName);
        List<String> filteredResults = new ArrayList<>();
        try {
            Document document = Jsoup.parse(file, "UTF-8");
            document.getAllElements().stream().filter(this::filterLines).filter(q -> !q.toString().startsWith("--")).forEach(e -> {

                String cleanResult = cleanHtmlResult(e.toString());
                cleanResult = cleanBrackets(cleanResult);
                cleanResult = cleanSplit(cleanResult);
                cleanResult = cleanLineHolder(cleanResult);

                if(!cleanResult.isEmpty()) {
                    LOG.debug("LINE: " + cleanResult);
                    filteredResults.add(cleanResult);
                }
//              LOG.info("LINE: " + e.toString());
            });
        } catch (IOException e) {
            LOG.error("Could not parse " + originFileName, e);
        }
        LOG.info("parsed " + originFileName + ". result count: " + filteredResults.size());
        return filteredResults;
    }

    protected boolean filterLines(Element e) {
        return e.toString().startsWith(matchingPatternHolder.getLtfEven()) || e.toString().startsWith(matchingPatternHolder.getLtfOdd());
    }

    private String cleanHtmlResult(final String html) {

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < html.length(); i++) {
            final char c = html.charAt(i);
            if(!Character.isAlphabetic(c) && !matchingPatternHolder.getInvalidChars().contains(c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    private String cleanSplit(final String s) {
        String r = s;
        while (r.contains(matchingPatternHolder.getSplit() + matchingPatternHolder.getSplit())) {
            r = r.replace(matchingPatternHolder.getSplit() + matchingPatternHolder.getSplit(), matchingPatternHolder.getSplit());
        }
        return r;
    }

    private String cleanBrackets(String s) {
        StringBuilder builder = new StringBuilder();
        boolean isInsideTag = false;
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == '<')
            {
                isInsideTag = true;
                continue;
            }
            if(s.charAt(i) == '>')
            {
                isInsideTag = false;
                builder.append(matchingPatternHolder.getSplit());
                continue;
            }
            if(!isInsideTag) {
                builder.append(s.charAt(i));
            }
        }
        return builder.toString();
    }

    private String cleanLineHolder(String result) {
        if(result.trim().equalsIgnoreCase(matchingPatternHolder.getSplit())) {
            result = "";
        }
        return result;
    }

    public MatchingPatternHolder getMatchingPatternHolder() {
        return matchingPatternHolder;
    }
}
