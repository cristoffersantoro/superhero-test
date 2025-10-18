package com.example.superhero.application.dto;

import com.example.superhero.domain.model.Hero;
import com.example.superhero.domain.model.HeroSuperpower;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HeroiDTOs {
    public record HeroiCreateDTO(
            @NotBlank(message = "{heroi.nome.notblank}")
            @Size(max = 120, message = "{heroi.nome.size}")
            String nome,

            @NotBlank(message = "{heroi.nomeHeroi.notblank}")
            @Size(max = 120, message = "{heroi.nomeHeroi.size}")
            String nomeHeroi,

            @Past(message = "{heroi.dataNascimento.past}")
            Instant dataNascimento,

            @NotNull(message = "{heroi.altura.notnull}")
            @Positive(message = "{heroi.altura.positive}")
            Float altura,

            @NotNull(message = "{heroi.peso.notnull}")
            @Positive(message = "{heroi.peso.positive}")
            Float peso,

            @NotNull(message = "{heroi.superpoderes.notnull}")
            @Size(min = 1, message = "{heroi.superpoderes.min}")
            List<@NotNull(message = "{heroi.superpoderes.element.notnull}") Long> superpoderesIds
    ) {}

    public record HeroiUpdateDTO(
            @NotBlank(message = "{heroi.nome.notblank}")
            @Size(max = 120, message = "{heroi.nome.size}")
            String nome,
            @NotBlank(message = "{heroi.nomeHeroi.notblank}")
            @Size(max = 120, message = "{heroi.nomeHeroi.size}")
            String nomeHeroi,
            @Past(message = "{heroi.dataNascimento.past}")
            Instant dataNascimento,
            @NotNull(message = "{heroi.altura.notnull}") @Positive(message = "{heroi.altura.positive}") Float altura,
            @NotNull(message = "{heroi.peso.notnull}") @Positive(message = "{heroi.peso.positive}") Float peso,
            @NotNull(message = "{heroi.superpoderes.notnull}") @Size(min = 1, message = "{heroi.superpoderes.min}")
            List<@NotNull(message = "{heroi.superpoderes.element.notnull}") Long> superpoderesIds
    ) {}

    public record HeroiResponseDTO(
            Long id,
            String nome,
            String nomeHeroi,
            Instant dataNascimento,
            Float altura,
            Float peso,
            Set<Long> superpoderesIds
    ) {
        public static HeroiResponseDTO fromDomain(Hero h) {
            return new HeroiResponseDTO(
                    h.getId(),
                    h.getNome(),
                    h.getNomeHeroi(),
                    h.getDataNascimento(),
                    h.getAltura(),
                    h.getPeso(),
                    h.getAssociations().stream().map(HeroSuperpower::getSuperpowerId).collect(Collectors.toSet())
            );
        }
    }
}