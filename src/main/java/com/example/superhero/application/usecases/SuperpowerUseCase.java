package com.example.superhero.application.usecases;

import com.example.superhero.domain.model.Superpower;

import java.util.List;

public interface SuperpowerUseCase {
    Superpower criar(String superpoder, String descricao);
    Superpower atualizar(Long id, String superpoder, String descricao);
    void deletar(Long id);
    Superpower buscar(Long id);
    List<Superpower> listar();
}
