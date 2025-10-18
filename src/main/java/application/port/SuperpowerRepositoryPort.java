package application.port;

import domain.model.Superpower;

import java.util.List;
import java.util.Optional;

public interface SuperpowerRepositoryPort {
    List<Superpower> findAll();
    Optional<Superpower> findById(Long id);
    List<Superpower> findAllById(Iterable<Long> ids);
    Superpower save(Superpower s);
    void deleteById(Long id);
    boolean existsBySuperpoderIgnoreCase(String name);
}
