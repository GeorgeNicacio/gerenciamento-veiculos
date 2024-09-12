package br.com.fcamara.controleveiculos.service;

import java.util.List;

import br.com.fcamara.controleveiculos.dtos.MovimentacaoVeiculoDTO;
import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;

public interface MovimentacaoVeiculoService {
	MovimentacaoVeiculoDTO registrarMovimentacao(Long empresaId,Long veiculoId, TipoMovimentacao tipoMovimentacao);
    List<MovimentacaoVeiculoDTO> listarMovimentacoes();
}
