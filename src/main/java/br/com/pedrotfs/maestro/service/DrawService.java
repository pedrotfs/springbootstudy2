package br.com.pedrotfs.maestro.service;

import br.com.pedrotfs.maestro.domain.Draw;
import br.com.pedrotfs.maestro.exception.EntityIdNotFoundException;

import java.util.List;

public interface DrawService {

    Draw createDraw(final String _id, final String registerId, final String date, final String[] numbers, final String[] dividends, final String[] amount);

    Draw getSingleDraws(final String _id) throws EntityIdNotFoundException;

    List<Draw> getDraws();

    List<Draw> getDrawsByRegisterId(final String registerId);

    Draw updateDraws(final Draw draw) throws EntityIdNotFoundException;

    String deleteDraws(final String _id) throws EntityIdNotFoundException;

    List<Draw> findByRegisterIdAndNumberIn(final String registerId, final String[] numbers);

    void populateDraw(String _id, String registerId, String date, String[] numbers, String[] dividends, String[] amount, Draw draw);
}
