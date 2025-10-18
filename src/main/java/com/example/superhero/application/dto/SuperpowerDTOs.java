package com.example.superhero.application.dto;

import com.example.superhero.domain.model.Superpower;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SuperpowerDTOs {
    public record SuperpoderCreateDTO(
            @NotBlank(message = "{superpoder.nome.notblank}")
            @Size(max = 50, message = "{superpoder.nome.size}")
            String superpoder,
            @Size(max = 250, message = "{superpoder.descricao.size}")
            String descricao
    ) {}

    public record SuperpoderUpdateDTO(
            @NotBlank(message = "{superpoder.nome.notblank}")
            @Size(max = 50, message = "{superpoder.nome.size}")
            String superpoder,
            @Size(max = 250, message = "{superpoder.descricao.size}")
            String descricao
    ) {}

    public record SuperpoderResponseDTO(Long id, String superpoder, String descricao) {
        public static SuperpoderResponseDTO fromEntity(Superpower s) {
            return new SuperpoderResponseDTO(s.getId(), s.getSuperpoder(), s.getDescricao());
        }
    }
}
