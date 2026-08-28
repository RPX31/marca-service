package com.gestorprodutos.marca.service;

import com.gestorprodutos.marca.domain.entity.Marca;
import com.gestorprodutos.marca.domain.repository.MarcaRepository;
import com.gestorprodutos.marca.exception.BusinessException;
import com.gestorprodutos.marca.exception.ResourceNotFoundException;
import com.gestorprodutos.marca.exception.ResourceInUseException;


import com.gestorprodutos.marca.infrastructure.restClient.ProdutoRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcaService {

    private final MarcaRepository marcaRepository;
    private final ProdutoRestClient produtoRestClient;

    @Transactional(readOnly = true)
    public List<Marca> listarTodos() {
        return marcaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Marca> buscarPorNome(String nome) {
        return marcaRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public Marca buscarPorId(Long id) {

        return marcaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Marca não encontrada com o ID: " + id
                        )
                );
    }

    @Transactional
    public Marca salvar(Marca marca) {

        validarNomeDuplicado(marca.getNome(), null);

        return marcaRepository.save(marca);
    }

    @Transactional
    public Marca atualizar(Long id, Marca marcaAtualizada) {

        Marca marcaExistente = buscarPorId(id);

        validarNomeDuplicado(
                marcaAtualizada.getNome(),
                id
        );

        marcaExistente.setNome(marcaAtualizada.getNome());

        return marcaRepository.save(marcaExistente);
    }

    @Transactional
    public void deletar(Long id) {

        Marca marca = buscarPorId(id);

        if (produtoRestClient.existeProdutoPorMarca(id)) {

            throw new ResourceInUseException(
                    "Não é possível excluir a marca '" +
                            marca.getNome() +
                            "' porque existem produtos associados a ela."
            );
        }

        marcaRepository.delete(marca);
    }

    private void validarNomeDuplicado(String nome, Long id) {

        marcaRepository
                .findByNomeIgnoreCase(nome)
                .ifPresent(marca -> {

                    if (id == null || !marca.getId().equals(id)) {

                        throw new BusinessException(
                                "Já existe uma marca com esse nome."
                        );
                    }
                });
    }
}