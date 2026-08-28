package com.gestorprodutos.marca.domain.repository;

import com.gestorprodutos.marca.domain.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarcaRepository extends JpaRepository<Marca, Long> {

    List<Marca> findByNomeContainingIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    Optional<Marca> findByNomeIgnoreCase(String nome);
}