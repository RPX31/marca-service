package com.gestorprodutos.marca.controller;

import com.gestorprodutos.marca.dto.request.MarcaRequestDTO;
import com.gestorprodutos.marca.dto.response.MarcaResponseDTO;
import com.gestorprodutos.marca.service.MarcaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/marcas")
public class MarcaController {

    private final MarcaService marcaService;

    @GetMapping
    public ResponseEntity<List<MarcaResponseDTO>> listar() {

        return ResponseEntity.ok(
                marcaService.listarTodos()
        );
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<MarcaResponseDTO>> buscarPorNome(
            @RequestParam String nome) {

        return ResponseEntity.ok(
                marcaService.buscarPorNome(nome)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        MarcaResponseDTO marca =
                marcaService.buscarPorId(id);

        return ResponseEntity.ok(marca);
    }

    @PostMapping
    public ResponseEntity<MarcaResponseDTO> salvar(
            @Valid @RequestBody MarcaRequestDTO request) {

        return ResponseEntity.ok(
                marcaService.salvar(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MarcaRequestDTO request) {

        return ResponseEntity.ok(
                marcaService.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        marcaService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}