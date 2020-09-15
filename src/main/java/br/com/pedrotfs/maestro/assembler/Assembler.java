package br.com.pedrotfs.maestro.assembler;

import br.com.pedrotfs.maestro.domain.Draw;

import java.util.List;

public interface Assembler {
    List<Draw> assemble(List<String> list);

    List<Draw> assembleMgs(List<String> list);
}
