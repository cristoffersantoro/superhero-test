package com.example.superhero.application.usecases;

import com.example.superhero.application.port.HeroRepositoryPort;
import com.example.superhero.application.port.SuperpowerRepositoryPort;
import com.example.superhero.domain.model.Hero;
import com.example.superhero.domain.model.HeroSuperpower;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HeroUseCaseImpl implements HeroUseCase {
    private final HeroRepositoryPort heroRepo;
    private final SuperpowerRepositoryPort powerRepo; // opcional: validação

    public HeroUseCaseImpl(HeroRepositoryPort heroRepo, SuperpowerRepositoryPort powerRepo) {
        this.heroRepo = heroRepo;
        this.powerRepo = powerRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hero> listar() { return heroRepo.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Hero buscar(Long id) {
        return heroRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("heroi.naoEncontrado"));
    }

    @Override
    @Transactional
    public Hero criar(String nome, String nomeHeroi, Instant dataNascimento, Float altura, Float peso, List<Long> superpoderesIds) {
        Hero h = new Hero(null, nome, nomeHeroi, dataNascimento, altura, peso);
        h.replaceAssociations(toAssociations(h, superpoderesIds));
        validateSuperpowersExist(superpoderesIds);
        return heroRepo.save(h);
    }

    @Override
    @Transactional
    public Hero atualizar(Long id, String nome, String nomeHeroi, Instant dataNascimento, Float altura, Float peso, List<Long> superpoderesIds) {
        Hero h = buscar(id);
        h.setNome(nome);
        h.setNomeHeroi(nomeHeroi);
        h.setDataNascimento(dataNascimento);
        h.setAltura(altura);
        h.setPeso(peso);
        h.replaceAssociations(toAssociations(h, superpoderesIds));
        validateSuperpowersExist(superpoderesIds);
        return heroRepo.save(h);
    }

    @Override
    @Transactional
    public void deletar(Long id) { heroRepo.deleteById(id); }

    @Override
    @Transactional
    public Hero adicionarSuperpoder(Long heroiId, Long superpoderId) {
        Hero h = buscar(heroiId);
        h.addAssociation(new HeroSuperpower(h, superpoderId));
        validateSuperpowersExist(java.util.List.of(superpoderId));
        return heroRepo.save(h);
    }

    @Override
    @Transactional
    public Hero removerSuperpoder(Long heroiId, Long superpoderId) {
        Hero h = buscar(heroiId);
        h.removeAssociationBySuperpowerId(superpoderId);
        return heroRepo.save(h);
    }

    private Set<HeroSuperpower> toAssociations(Hero owner, List<Long> ids) {
        if (ids == null) return java.util.Collections.emptySet();
        return ids.stream().map(id -> new HeroSuperpower(owner, id)).collect(Collectors.toCollection(HashSet::new));
    }

    private void validateSuperpowersExist(List<Long> ids) {
        if (ids == null || ids.isEmpty()) throw new EntityNotFoundException("superpoder.listaVaziaOuInvalida");
        var found = powerRepo.findAllById(ids);
        if (found.size() != ids.size()) throw new EntityNotFoundException("superpoder.listaVaziaOuInvalida");
    }
}
