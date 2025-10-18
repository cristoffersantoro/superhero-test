package com.example.superhero.infrastructure.web.controller;

import com.example.superhero.application.dto.HeroiDTOs;
import com.example.superhero.application.usecases.HeroUseCase;
import com.example.superhero.infrastructure.web.error.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.stream.Collectors;

@Tag(name = "Heróis", description = "CRUD de heróis e vínculo/desvínculo de superpoderes")
@RestController
@RequestMapping("/herois")
public class HeroController {

    private final HeroUseCase useCase;

    public HeroController(HeroUseCase useCase) {
        this.useCase = useCase;
    }

    @Operation(
            summary = "Cria um herói",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = HeroiDTOs.HeroiCreateDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo criação",
                                    value = "{\n" +
                                            "  \"nome\": \"Peter Parker\",\n" +
                                            "  \"nomeHeroi\": \"Homem-Aranha\",\n" +
                                            "  \"dataNascimento\": \"2001-08-10T00:00:00Z\",\n" +
                                            "  \"altura\": 1.78,\n" +
                                            "  \"peso\": 76.0,\n" +
                                            "  \"superpoderesIds\": [1, 2]\n" +
                                            "}"
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Criado"),
            @ApiResponse(responseCode = "422", description = "Erro de validação",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<HeroiDTOs.HeroiResponseDTO> criar(
            @Valid @RequestBody HeroiDTOs.HeroiCreateDTO dto) {
        var saved = useCase.criar(dto.nome(), dto.nomeHeroi(), dto.dataNascimento(), dto.altura(), dto.peso(), dto.superpoderesIds());
        return ResponseEntity.created(URI.create("/herois/" + saved.getId()))
                .body(HeroiDTOs.HeroiResponseDTO.fromDomain(saved));
    }

    @Operation(summary = "Lista heróis")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping
    public ResponseEntity<?> listar() {
        var list = useCase.listar().stream()
                .map(HeroiDTOs.HeroiResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Busca herói por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Herói não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<HeroiDTOs.HeroiResponseDTO> buscar(
            @Parameter(description = "ID do herói", example = "1")
            @PathVariable Long id) {
        var h = useCase.buscar(id);
        return ResponseEntity.ok(HeroiDTOs.HeroiResponseDTO.fromDomain(h));
    }

    @Operation(
            summary = "Atualiza um herói",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = HeroiDTOs.HeroiUpdateDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo atualização",
                                    value = "{\n" +
                                            "  \"nome\": \"Peter B. Parker\",\n" +
                                            "  \"nomeHeroi\": \"Homem-Aranha\",\n" +
                                            "  \"dataNascimento\": \"2001-08-10T00:00:00Z\",\n" +
                                            "  \"altura\": 1.79,\n" +
                                            "  \"peso\": 77.0,\n" +
                                            "  \"superpoderesIds\": [1, 3]\n" +
                                            "}"
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atualizado"),
            @ApiResponse(responseCode = "404", description = "Herói não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "422", description = "Erro de validação",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<HeroiDTOs.HeroiResponseDTO> atualizar(
            @Parameter(description = "ID do herói", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody HeroiDTOs.HeroiUpdateDTO dto) {
        var h = useCase.atualizar(id, dto.nome(), dto.nomeHeroi(), dto.dataNascimento(), dto.altura(), dto.peso(), dto.superpoderesIds());
        return ResponseEntity.ok(HeroiDTOs.HeroiResponseDTO.fromDomain(h));
    }

    @Operation(summary = "Remove um herói")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Removido"),
            @ApiResponse(responseCode = "404", description = "Herói não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do herói", example = "1")
            @PathVariable Long id) {
        useCase.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Adiciona superpoder ao herói")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vínculo criado/atualizado"),
            @ApiResponse(responseCode = "404", description = "Herói ou superpoder não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping("/{id}/superpoderes/{superpoderId}")
    public ResponseEntity<HeroiDTOs.HeroiResponseDTO> adicionarSuperpoder(
            @Parameter(description = "ID do herói", example = "1")
            @PathVariable Long id,
            @Parameter(description = "ID do superpoder", example = "2")
            @PathVariable Long superpoderId) {
        var h = useCase.adicionarSuperpoder(id, superpoderId);
        return ResponseEntity.ok(HeroiDTOs.HeroiResponseDTO.fromDomain(h));
    }

    @Operation(summary = "Remove superpoder do herói")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vínculo removido"),
            @ApiResponse(responseCode = "404", description = "Herói não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}/superpoderes/{superpoderId}")
    public ResponseEntity<HeroiDTOs.HeroiResponseDTO> removerSuperpoder(
            @Parameter(description = "ID do herói", example = "1")
            @PathVariable Long id,
            @Parameter(description = "ID do superpoder", example = "2")
            @PathVariable Long superpoderId) {
        var h = useCase.removerSuperpoder(id, superpoderId);
        return ResponseEntity.ok(HeroiDTOs.HeroiResponseDTO.fromDomain(h));
    }
}