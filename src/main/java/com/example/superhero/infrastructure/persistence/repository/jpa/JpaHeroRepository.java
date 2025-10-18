package com.example.superhero.infrastructure.persistence.repository.jpa;

import com.example.superhero.infrastructure.persistence.entity.JpaHeroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaHeroRepository extends JpaRepository<JpaHeroEntity, Long> {
    boolean existsByNomeHeroiIgnoreCase(String nomeHeroi);
}
