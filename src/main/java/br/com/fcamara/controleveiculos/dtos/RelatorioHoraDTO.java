package br.com.fcamara.controleveiculos.dtos;

import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RelatorioHoraDTO {
	private int hora;
    private TipoMovimentacao tipo;
    private long qtd;
}
