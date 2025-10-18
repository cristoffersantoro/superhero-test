package com.example.superhero.infrastructure.persistence.adapter;

import com.example.superhero.application.port.HeroRepositoryPort;
import com.example.superhero.domain.model.Hero;
import com.example.superhero.domain.model.HeroSuperpower;
import com.example.superhero.infrastructure.persistence.entity.JpaHeroEntity;
import com.example.superhero.infrastructure.persistence.entity.JpaHeroSuperpowerEntity;
import com.example.superhero.infrastructure.persistence.entity.JpaSuperpowerEntity;
import com.example.superhero.infrastructure.persistence.repository.jpa.JpaHeroRepository;
import com.example.superhero.infrastructure.persistence.repository.jpa.JpaSuperpowerRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class HeroRepository implements HeroRepositoryPort {
    private final JpaHeroRepository heroJpa;
    private final JpaSuperpowerRepository powerJpa;

    public HeroRepository(JpaHeroRepository heroJpa, JpaSuperpowerRepository powerJpa) {
        this.heroJpa = heroJpa;
        this.powerJpa = powerJpa;

    }

    @Override
    @Transactional(readOnly = true)
    public List<Hero> findAll() {
        return heroJpa.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Hero> findById(Long id) {
        return heroJpa.findById(id).map(this::toDomain);
    }

    @Override
    @Transactional
    public Hero save(Hero hero) {
        JpaHeroEntity entity = (hero.getId() != null)
                ? heroJpa.findById(hero.getId()).orElseThrow() // se não existir, prefiro 404
                : new JpaHeroEntity();

        entity.setId(hero.getId());
        entity.setNome(hero.getNome());
        entity.setNomeHeroi(hero.getNomeHeroi());
        entity.setDataNascimento(hero.getDataNascimento());
        entity.setAltura(hero.getAltura());
        entity.setPeso(hero.getPeso());

        JpaHeroEntity managed = heroJpa.save(entity);

        final Set<Long> alvoIds = (hero.getAssociations() == null)
                ? Set.of()
                : hero.getAssociations().stream()
                .map(HeroSuperpower::getSuperpowerId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        managed.getAssociations().removeIf(link ->
                !alvoIds.contains(link.getSuperpoder().getId())
        );

        Set<Long> atuais = managed.getAssociations().stream()
                .map(l -> l.getSuperpoder().getId())
                .collect(Collectors.toSet());

        for (Long spId : alvoIds) {
            if (!atuais.contains(spId)) {
                JpaSuperpowerEntity spRef = powerJpa.getReferenceById(spId); // ou findById(...).orElseThrow()
                JpaHeroSuperpowerEntity link = new JpaHeroSuperpowerEntity();
                link.setHeroi(managed);
                link.setSuperpoder(spRef);
                managed.getAssociations().add(link);
            }
        }

        return toDomain(managed);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        heroJpa.deleteById(id);
    }

    @Override
    public boolean existsByNomeHeroiIgnoreCase(String nomeHeroi) {
        return heroJpa.existsByNomeHeroiIgnoreCase(nomeHeroi);
    }

    // -------------------- mapeamentos --------------------
    private Hero toDomain(JpaHeroEntity e) {
        Hero d = new Hero();
        d.setId(e.getId());
        d.setNome(e.getNome());
        d.setNomeHeroi(e.getNomeHeroi());
        d.setDataNascimento(e.getDataNascimento());
        d.setAltura(e.getAltura());
        d.setPeso(e.getPeso());

        if (e.getAssociations() != null) {
            Set<HeroSuperpower> assocs = e.getAssociations().stream()
                    .map(j -> {
                        HeroSuperpower hs = new HeroSuperpower();
                        hs.setHero(d); // mantém consistência do agregado
                        hs.setSuperpowerId(j.getSuperpoder().getId());
                        return hs;
                    }).collect(Collectors.toSet());
            d.setAssociations(assocs);
        }
        return d;
    }
}
