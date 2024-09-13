package br.com.fcamara.controleveiculos.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Data
@JacksonXmlRootElement(localName = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "\\d{14}", message = "CNPJ deve ter 14 dígitos")
    @Column(unique = true)
    private String cnpj;

    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Telefone deve seguir o padrão (XX) XXXX-XXXX ou (XX) XXXXX-XXXX")
    private String telefone;

    @NotNull(message = "Quantidade de vagas para motos é obrigatória")
    private Integer vagasMotos;

    @NotNull(message = "Quantidade de vagas para carros é obrigatória")
    private Integer vagasCarros;
    
    @ManyToMany
    @JoinTable(
        name = "empresa_veiculo",
        joinColumns = @JoinColumn(name = "empresa_id"),
        inverseJoinColumns = @JoinColumn(name = "veiculo_id")
    )
    private Set<Veiculo> veiculos = new HashSet<>();
    
    // Métodos auxiliares para gerenciar o relacionamento
    public void addVeiculo(Veiculo veiculo) {
        this.veiculos.add(veiculo);
        veiculo.getEmpresas().add(this);
    }

    public void removeVeiculo(Veiculo veiculo) {
        this.veiculos.remove(veiculo);
        veiculo.getEmpresas().remove(this);
    }
}