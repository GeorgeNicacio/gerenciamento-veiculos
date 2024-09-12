package br.com.fcamara.controleveiculos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import br.com.fcamara.controleveiculos.dtos.MovimentacaoVeiculoDTO;
import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;
import br.com.fcamara.controleveiculos.service.MovimentacaoVeiculoService;

@Controller
public class MovimentacaoVeiculoController {

    @Autowired
    private MovimentacaoVeiculoService movimentacaoVeiculoService;

    @MutationMapping
    public MovimentacaoVeiculoDTO registrarEntrada(@Argument Long empresaId,@Argument Long veiculoId) {
    	MovimentacaoVeiculoDTO movimentacao = movimentacaoVeiculoService.registrarMovimentacao(empresaId,veiculoId, TipoMovimentacao.ENTRADA);
        return movimentacao;
    }

    @MutationMapping
    public MovimentacaoVeiculoDTO registrarSaida(@Argument Long empresaId, @Argument Long veiculoId) {
    	MovimentacaoVeiculoDTO movimentacao = movimentacaoVeiculoService.registrarMovimentacao(empresaId,veiculoId, TipoMovimentacao.SAIDA);
        return movimentacao;
    }

    @QueryMapping
    public List<MovimentacaoVeiculoDTO> listarMovimentacoes() {
        return movimentacaoVeiculoService.listarMovimentacoes();
    }
}
