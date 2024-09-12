package br.com.fcamara.controleveiculos.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import br.com.fcamara.controleveiculos.dtos.MovimentacaoVeiculoDTO;
import br.com.fcamara.controleveiculos.dtos.RelatorioHoraDTO;
import br.com.fcamara.controleveiculos.dtos.RelatorioInput;
import br.com.fcamara.controleveiculos.dtos.RelatorioSumarioDTO;
import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;
import br.com.fcamara.controleveiculos.service.RelatorioService;

@Controller
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    // Query para gerar o relatório de movimentações por data
    @QueryMapping
    public List<MovimentacaoVeiculoDTO> gerarRelatorioMovimentacao(@Argument RelatorioInput input) {
        // Chama o serviço para obter o relatório de movimentações por período
        return relatorioService.gerarRelatorioMovimentacao(input.getEmpresaId(), input.getDataInicio(), input.getDataFim());
    }

    // Query para gerar o relatório de movimentações por hora
    @QueryMapping
    public List<RelatorioHoraDTO> gerarRelatorioMovimentacaoPorHora(@Argument RelatorioInput input) {
        // Chama o serviço e converte os resultados no formato adequado
        List<Object[]> relatorioPorHora = relatorioService.gerarRelatorioMovimentacaoPorHora(input.getEmpresaId(), input.getDataInicio(), input.getDataFim());

        // Converte o resultado para o formato DTO (hora, entradas, saídas)
        return relatorioPorHora.stream()
                .map(obj -> new RelatorioHoraDTO((Integer) obj[0], (TipoMovimentacao) obj[1], (Long) obj[2]))
                .collect(Collectors.toList());
    }

    // Query para obter sumário de entradas e saídas por período
    @QueryMapping
    public RelatorioSumarioDTO obterSumarioEntradaSaida( @Argument RelatorioInput input) {
        // Obtém o número de entradas e saídas no período
        long entradas = relatorioService.contarEntradasPorPeriodo(input.getEmpresaId(), input.getDataInicio(), input.getDataFim());
        long saidas = relatorioService.contarSaidasPorPeriodo(input.getEmpresaId(), input.getDataInicio(), input.getDataFim());

        // Retorna um DTO contendo o sumário de entradas e saídas
        return new RelatorioSumarioDTO(entradas, saidas);
    }

}