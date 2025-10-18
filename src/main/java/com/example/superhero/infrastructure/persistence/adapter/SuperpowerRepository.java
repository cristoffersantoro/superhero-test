package com.example.superhero.infrastructure.persistence.adapter;

import com.example.superhero.application.port.SuperpowerRepositoryPort;
import com.example.superhero.domain.model.Superpower;
import com.example.superhero.infrastructure.persistence.entity.JpaSuperpowerEntity;
import com.example.superhero.infrastructure.persistence.repository.jpa.JpaSuperpowerRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SuperpowerRepository implements SuperpowerRepositoryPort {

    private final JpaSuperpowerRepository repo;

    public SuperpowerRepository(JpaSuperpowerRepository repo) { this.repo = repo; }

    @Override
    @Transactional(readOnly = true)
    public List<Superpower> findAll() {
        return repo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Superpower> findById(Long id) {
        return repo.findById(id).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Superpower> findAllById(Iterable<Long> ids) {
        return repo.findAllById(ids).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Superpower save(Superpower s) {
        JpaSuperpowerEntity e = new JpaSuperpowerEntity();
        e.setId(s.getId());
        e.setSuperpoder(s.getSuperpoder());
        e.setDescricao(s.getDescricao());
        return toDomain(repo.save(e));
    }

    @Override
    @Transactional
    public void deleteById(Long id) { repo.deleteById(id); }

    @Override
    public boolean existsBySuperpoderIgnoreCase(String name) { return repo.existsBySuperpoderIgnoreCase(name); }

    private Superpower toDomain(JpaSuperpowerEntity e) {
        return new Superpower(e.getId(), e.getSuperpoder(), e.getDescricao());
    }
}
