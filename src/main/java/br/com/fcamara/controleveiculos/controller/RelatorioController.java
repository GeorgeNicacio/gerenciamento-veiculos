package br.com.fcamara.controleveiculos.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fcamara.controleveiculos.dtos.MovimentacaoVeiculoDTO;
import br.com.fcamara.controleveiculos.service.RelatorioService;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;
    
 // Endpoint para relatório de movimentações por data
    @GetMapping("/movimentacoes")
    public ResponseEntity<List<MovimentacaoVeiculoDTO>> gerarRelatorioMovimentacao(
            @RequestParam Long empresaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        List<MovimentacaoVeiculoDTO> movimentacoes = relatorioService.gerarRelatorioMovimentacao(empresaId, dataInicio, dataFim);
        return ResponseEntity.ok(movimentacoes);
    }

    // Endpoint para relatório por hora de movimentações
    @GetMapping("/movimentacoes-por-hora")
    public ResponseEntity<List<Object[]>> gerarRelatorioMovimentacaoPorHora(
            @RequestParam Long empresaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        List<Object[]> relatorioPorHora = relatorioService.gerarRelatorioMovimentacaoPorHora(empresaId, dataInicio, dataFim);
        return ResponseEntity.ok(relatorioPorHora);
    }

    @GetMapping("/sumario")
    public ResponseEntity<Map<String, Long>> obterSumarioEntradaSaida(
    		@RequestParam Long empresaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {

        long entradas = relatorioService.contarEntradasPorPeriodo(empresaId, dataInicio, dataFim);
        long saidas = relatorioService.contarSaidasPorPeriodo(empresaId, dataInicio, dataFim);

        Map<String, Long> resultado = new HashMap<>();
        resultado.put("entradas", entradas);
        resultado.put("saidas", saidas);

        return ResponseEntity.ok(resultado);
    }

}
