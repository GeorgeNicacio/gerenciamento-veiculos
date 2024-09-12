package br.com.fcamara.controleveiculos.dtos;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.fcamara.controleveiculos.model.Veiculo;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class EmpresaDTO {
	private Long id;
    private String nome;
    private String cnpj;
    private String endereco;
    private String telefone;
    private Integer vagasMotos;
    private Integer vagasCarros;
    private Set<Veiculo> veiculos = new HashSet<>();
}