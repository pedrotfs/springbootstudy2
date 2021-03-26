package br.com.pedrotfs.maestro.facade;

import br.com.pedrotfs.maestro.exception.EntityIdNotFoundException;

public interface UpdateFacade {

    void update(String buttonId) throws EntityIdNotFoundException, InterruptedException;
}
