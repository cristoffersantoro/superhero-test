package com.example.superhero.infrastructure.web.controller;

import com.example.superhero.application.dto.SuperpowerDTOs;
import com.example.superhero.application.usecases.SuperpowerUseCase;
import com.example.superhero.infrastructure.web.error.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.stream.Collectors;

@Tag(name = "Superpoderes", description = "CRUD de superpoderes")
@RestController
@RequestMapping("/superpoderes")
public class SuperpowerController {

    private final SuperpowerUseCase useCase;

    public SuperpowerController(SuperpowerUseCase useCase) {
        this.useCase = useCase;
    }

    @Operation(
            summary = "Cria um superpoder",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SuperpowerDTOs.SuperpoderCreateDTO.class),
                            examples = @ExampleObject(
                                    name = "ex1",
                                    value = "{ \"superpoder\": \"Voo\", \"descricao\": \"Capacidade de voar\" }"
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
    public ResponseEntity<SuperpowerDTOs.SuperpoderResponseDTO> criar(
            @Valid @RequestBody SuperpowerDTOs.SuperpoderCreateDTO dto) {
        var s = useCase.criar(dto.superpoder(), dto.descricao());
        return ResponseEntity.created(URI.create("/superpoderes/" + s.getId()))
                .body(new SuperpowerDTOs.SuperpoderResponseDTO(s.getId(), s.getSuperpoder(), s.getDescricao()));
    }

    @Operation(summary = "Lista superpoderes")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping
    public ResponseEntity<?> listar() {
        var list = useCase.listar().stream()
                .map(s -> new SuperpowerDTOs.SuperpoderResponseDTO(s.getId(), s.getSuperpoder(), s.getDescricao()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Busca superpoder por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SuperpowerDTOs.SuperpoderResponseDTO> buscar(
            @Parameter(description = "ID do superpoder", example = "1")
            @PathVariable Long id) {
        var s = useCase.buscar(id);
        return ResponseEntity.ok(new SuperpowerDTOs.SuperpoderResponseDTO(s.getId(), s.getSuperpoder(), s.getDescricao()));
    }

    @Operation(summary = "Atualiza um superpoder")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atualizado"),
            @ApiResponse(responseCode = "404", description = "Não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "422", description = "Erro de validação",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<SuperpowerDTOs.SuperpoderResponseDTO> atualizar(
            @Parameter(description = "ID do superpoder", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody SuperpowerDTOs.SuperpoderUpdateDTO dto) {
        var s = useCase.atualizar(id, dto.superpoder(), dto.descricao());
        return ResponseEntity.ok(new SuperpowerDTOs.SuperpoderResponseDTO(s.getId(), s.getSuperpoder(), s.getDescricao()));
    }

    @Operation(summary = "Remove um superpoder")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Removido"),
            @ApiResponse(responseCode = "404", description = "Não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do superpoder", example = "1")
            @PathVariable Long id) {
        useCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
}