package br.com.pedrotfs.maestro.repository;

import br.com.pedrotfs.maestro.domain.Register;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegisterRepository extends MongoRepository<Register, String> {
}
