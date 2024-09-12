package br.com.fcamara.controleveiculos.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

import br.com.fcamara.controleveiculos.model.enums.TipoVeiculo;

@Data
public class VeiculoDTO {
    @NotBlank(message = "Marca é obrigatória")
    private String marca;

    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;

    @NotBlank(message = "Cor é obrigatória")
    private String cor;

    @NotBlank(message = "Placa é obrigatória")
    private String placa;

    @NotBlank(message = "Tipo é obrigatório")
    private TipoVeiculo tipo;

    private List<Long> empresaIds; // Lista opcional de IDs de Empresas para associação
}