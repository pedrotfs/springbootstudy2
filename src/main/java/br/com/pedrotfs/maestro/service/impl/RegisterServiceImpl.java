package br.com.pedrotfs.maestro.service.impl;

import br.com.pedrotfs.maestro.domain.Register;
import br.com.pedrotfs.maestro.exception.EntityIdNotFoundException;
import br.com.pedrotfs.maestro.repository.RegisterRepository;
import br.com.pedrotfs.maestro.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private RegisterRepository registerRepository;

    @Override
    public Register createRegister(String _id, String count, String limit) {
        Register register = new Register();
        register.set_id(_id);
        register.setCount(Integer.parseInt(count));
        register.setLimit(Integer.parseInt(limit));
        registerRepository.save(register);
        return register;
    }

    @Override
    public Register getSingleRegister(final String _id) throws EntityIdNotFoundException {
        final Optional<Register> byId = registerRepository.findById(_id);
        if(byId.isPresent()) {
           return byId.get();
        } else {
            throw new EntityIdNotFoundException();
        }
    }

    @Override
    public List<Register> getRegisters() {
        return registerRepository.findAll();
    }

    @Override
    public Register updateRegister(final Register register) throws EntityIdNotFoundException {
        final Optional<Register> byId = registerRepository.findById(register.get_id());
        if(byId.isPresent()) {
            registerRepository.save(register);
            return register;
        } else {
            throw new EntityIdNotFoundException();
        }
    }

    @Override
    public String deleteRegister(String _id) throws EntityIdNotFoundException {
        final Optional<Register> byId = registerRepository.findById(_id);
        if(byId.isPresent()) {
            registerRepository.delete(byId.get());
            return _id;
        } else {
            throw new EntityIdNotFoundException();
        }
    }
}
