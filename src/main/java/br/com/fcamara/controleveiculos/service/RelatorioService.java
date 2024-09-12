package br.com.fcamara.controleveiculos.service;

import java.time.LocalDateTime;
import java.util.List;

import br.com.fcamara.controleveiculos.dtos.MovimentacaoVeiculoDTO;

public interface RelatorioService {
	List<MovimentacaoVeiculoDTO> gerarRelatorioMovimentacao(Long empresaId, LocalDateTime dataInicio,
			LocalDateTime dataFim);
	List<Object[]> gerarRelatorioMovimentacaoPorHora(Long empresaId, LocalDateTime dataInicio, LocalDateTime dataFim);
	long contarEntradasPorPeriodo(Long empresaId, LocalDateTime dataInicio, LocalDateTime dataFim);
	long contarSaidasPorPeriodo(Long empresaId, LocalDateTime dataInicio, LocalDateTime dataFim);
}