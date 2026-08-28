package com.gestorprodutos.marca.infrastructure.restClient;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProdutoRestClient {

    private final RestClient restClient;

    public ProdutoRestClient() {

        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
    }

    public boolean existeProdutoPorMarca(Long marcaId) {

        Boolean existe = restClient
                .get()
                .uri("/produtos/marca/{marcaId}/existe", marcaId)
                .retrieve()
                .body(Boolean.class);

        return Boolean.TRUE.equals(existe);
    }
}