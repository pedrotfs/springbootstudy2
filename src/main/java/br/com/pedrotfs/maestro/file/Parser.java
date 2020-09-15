package br.com.pedrotfs.maestro.file;

import java.util.List;

public interface Parser {

    List<String> parse(final String originFileName);
}
