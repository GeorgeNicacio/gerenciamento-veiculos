package br.com.fcamara.controleveiculos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RelatorioSumarioDTO {
	private long entradas;
    private long saidas;
}
