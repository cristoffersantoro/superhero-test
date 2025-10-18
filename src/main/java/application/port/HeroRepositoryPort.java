package application.port;

import domain.model.Hero;

import java.util.List;
import java.util.Optional;

public interface HeroRepositoryPort {
    List<Hero> findAll();
    Optional<Hero> findById(Long id);
    Hero save(Hero hero);
    void deleteById(Long id);
    boolean existsByNomeHeroiIgnoreCase(String nomeHeroi);
}
