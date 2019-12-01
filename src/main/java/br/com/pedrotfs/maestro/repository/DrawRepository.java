package br.com.pedrotfs.maestro.repository;

import br.com.pedrotfs.maestro.domain.Draw;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DrawRepository extends MongoRepository<Draw, String> {

    List<Draw> findByRegisterId(final String registerId);
}
