
package com.gestorprodutos.marca.service;

import com.gestorprodutos.marca.domain.entity.Marca;
import com.gestorprodutos.marca.domain.repository.MarcaRepository;
import com.gestorprodutos.marca.dto.request.MarcaRequestDTO;
import com.gestorprodutos.marca.dto.response.MarcaResponseDTO;
import com.gestorprodutos.marca.exception.BusinessException;
import com.gestorprodutos.marca.exception.ResourceInUseException;
import com.gestorprodutos.marca.exception.ResourceNotFoundException;
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
    public List<MarcaResponseDTO> listarTodos() {

        return marcaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MarcaResponseDTO> buscarPorNome(String nome) {

        return marcaRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public MarcaResponseDTO buscarPorId(Long id) {

        Marca marca = buscarEntidadePorId(id);

        return toResponseDTO(marca);
    }

    @Transactional
    public MarcaResponseDTO salvar(MarcaRequestDTO request) {

        validarNomeDuplicado(request.nome(), null);

        // ALTERAÇÃO: cria a entidade utilizando o construtor que recebe o DTO.
        Marca marca = new Marca(request);

        Marca marcaSalva = marcaRepository.save(marca);

        return toResponseDTO(marcaSalva);
    }

    @Transactional
    public MarcaResponseDTO atualizar(
            Long id,
            MarcaRequestDTO request
    ) {

        Marca marcaExistente = buscarEntidadePorId(id);

        validarNomeDuplicado(
                request.nome(),
                id
        );

        marcaExistente.setNome(request.nome());

        Marca marcaAtualizada = marcaRepository.save(marcaExistente);

        return toResponseDTO(marcaAtualizada);
    }

    @Transactional
    public void deletar(Long id) {

        Marca marca = buscarEntidadePorId(id);

        if (produtoRestClient.existeProdutoPorMarca(id)) {

            throw new ResourceInUseException(
                    "Não é possível excluir a marca '" +
                            marca.getNome() +
                            "' porque existem produtos associados a ela."
            );
        }

        marcaRepository.delete(marca);
    }

    private Marca buscarEntidadePorId(Long id) {

        return marcaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Marca não encontrada com o ID: " + id
                        )
                );
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

    private MarcaResponseDTO toResponseDTO(Marca marca) {

        return new MarcaResponseDTO(
                marca.getId(),
                marca.getNome()
        );
    }
}
