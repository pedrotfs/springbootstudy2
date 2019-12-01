package br.com.pedrotfs.maestro.service;

import br.com.pedrotfs.maestro.domain.Register;
import br.com.pedrotfs.maestro.exception.EntityIdNotFoundException;

import java.util.List;

public interface RegisterService {

    Register createRegister(final String _id, final String count, final String limit);

    Register getSingleRegister(final String _id) throws EntityIdNotFoundException;

    List<Register> getRegisters();

    Register updateRegister(final Register register) throws EntityIdNotFoundException;

    String deleteRegister(final String _id) throws EntityIdNotFoundException;
}
