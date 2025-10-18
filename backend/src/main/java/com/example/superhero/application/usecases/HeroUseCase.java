package com.example.superhero.application.usecases;

import com.example.superhero.domain.model.Hero;

import java.time.Instant;
import java.util.List;

public interface HeroUseCase {
    List<Hero> listar();
    Hero buscar(Long id);
    Hero criar(String nome, String nomeHeroi, Instant dataNascimento, Float altura, Float peso, List<Long> superpoderesIds);
    Hero atualizar(Long id, String nome, String nomeHeroi, Instant dataNascimento, Float altura, Float peso, List<Long> superpoderesIds);
    void deletar(Long id);
    Hero adicionarSuperpoder(Long heroiId, Long superpoderId);
    Hero removerSuperpoder(Long heroiId, Long superpoderId);
}
