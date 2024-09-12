package br.com.fcamara.controleveiculos.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import br.com.fcamara.controleveiculos.model.enums.TipoVeiculo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@JacksonXmlRootElement(localName = "veiculo")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Marca é obrigatória")
    private String marca;

    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;

    @NotBlank(message = "Cor é obrigatória")
    private String cor;

    @NotBlank(message = "Placa é obrigatória")
    @Column(unique = true)
    private String placa;

    @NotBlank(message = "Tipo é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipo;
    
    @ManyToMany(mappedBy = "veiculos")
    @JsonIgnore
    private Set<Empresa> empresas = new HashSet<>();
    
    // Métodos auxiliares para gerenciar o relacionamento
    public void addEmpresa(Empresa empresa) {
        this.empresas.add(empresa);
        empresa.getVeiculos().add(this);
    }

    public void removeEmpresa(Empresa empresa) {
        this.empresas.remove(empresa);
        empresa.getVeiculos().remove(this);
    }
}
