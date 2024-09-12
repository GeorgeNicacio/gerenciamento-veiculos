package br.com.fcamara.controleveiculos.dtos;

import java.time.LocalDateTime;

import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;
import lombok.Data;

@Data
public class MovimentacaoVeiculoDTO {
    private Long id;
    private VeiculoDTO veiculo;
    private EmpresaDTO empresa;
    private TipoMovimentacao tipoMovimentacao;
    private LocalDateTime dataHora;
}