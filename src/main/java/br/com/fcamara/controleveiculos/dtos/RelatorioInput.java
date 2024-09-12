package br.com.fcamara.controleveiculos.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RelatorioInput {
	private Long empresaId;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
}
