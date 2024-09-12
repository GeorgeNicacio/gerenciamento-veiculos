package br.com.fcamara.controleveiculos.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@JacksonXmlRootElement(localName = "movimentacaoVeiculo")
public class MovimentacaoVeiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Veículo é obrigatório")
    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;
    
    @NotNull(message = "Empresa é obrigatório")
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @NotNull(message = "Tipo de movimentação é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipoMovimentacao;

    @NotNull(message = "Data e hora são obrigatórios")
    private LocalDateTime dataHora;

    @PrePersist
    public void prePersist() {
        dataHora = LocalDateTime.now();
    }
}
