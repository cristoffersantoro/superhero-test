package com.example.superhero.application.usecases;

import com.example.superhero.application.port.SuperpowerRepositoryPort;
import com.example.superhero.domain.model.Superpower;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SuperpowerUseCaseImpl implements SuperpowerUseCase {

    private final SuperpowerRepositoryPort repo;

    public SuperpowerUseCaseImpl(SuperpowerRepositoryPort repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public Superpower criar(String superpoder, String descricao) {
        if (repo.existsBySuperpoderIgnoreCase(superpoder)) {
            // mapeie no ControllerAdvice para 409 se quiser
            throw new IllegalArgumentException("superpoder.nome.duplicado");
        }
        Superpower s = new Superpower(null, superpoder, descricao);
        return repo.save(s);
    }

    @Override
    @Transactional
    public Superpower atualizar(Long id, String superpoder, String descricao) {
        Superpower s = buscar(id); // lança 404 se não existir
        s.setSuperpoder(superpoder);
        s.setDescricao(descricao);
        return repo.save(s);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        // garante 404 consistente com seu Handler/i18n
        if (repo.findById(id).isEmpty()) {
            throw new EntityNotFoundException("superpoder.naoEncontrado");
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Superpower buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("superpoder.naoEncontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Superpower> listar() {
        return repo.findAll();
    }
}
