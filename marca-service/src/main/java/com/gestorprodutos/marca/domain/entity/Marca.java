
package com.gestorprodutos.marca.domain.entity;

import com.gestorprodutos.marca.dto.request.MarcaRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_marca")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O nome da Marca é obrigatório!")
    @Size(
            min = 3,
            max = 50,
            message = "O nome da marca deve ter entre 3 e 50 caracteres"
    )
    @Column(unique = true)
    private String nome;

    public Marca(MarcaRequestDTO request) {
        this.nome = request.nome();
    }
}
