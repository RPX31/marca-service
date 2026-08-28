package com.gestorprodutos.marca.controller;

import com.gestorprodutos.marca.domain.entity.Marca;
import com.gestorprodutos.marca.service.MarcaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/marcas")
public class MarcaController {

    private final MarcaService marcaService;

    @GetMapping
    public ResponseEntity<List<Marca>> listar() {

        return ResponseEntity.ok(
                marcaService.listarTodos()
        );
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Marca>> buscarPorNome(
            @RequestParam String nome) {

        return ResponseEntity.ok(
                marcaService.buscarPorNome(nome)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Marca> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                marcaService.buscarPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<Marca> salvar(
            @Valid @RequestBody Marca marca) {

        Marca marcaSalva = marcaService.salvar(marca);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(marcaSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Marca> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Marca marca) {

        return ResponseEntity.ok(
                marcaService.atualizar(id, marca)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        marcaService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}