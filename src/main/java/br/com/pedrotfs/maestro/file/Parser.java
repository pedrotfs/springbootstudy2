package br.com.pedrotfs.maestro.file;

import br.com.pedrotfs.maestro.domain.Draw;

import java.util.List;

public interface Parser {

    List<Draw> parse(final String originFileName, final String register, long startFrom);
}
