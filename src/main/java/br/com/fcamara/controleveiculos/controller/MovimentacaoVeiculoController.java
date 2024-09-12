package br.com.fcamara.controleveiculos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fcamara.controleveiculos.dtos.MovimentacaoVeiculoDTO;
import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;
import br.com.fcamara.controleveiculos.service.MovimentacaoVeiculoService;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoVeiculoController {

    @Autowired
    private MovimentacaoVeiculoService movimentacaoVeiculoService;

    @PostMapping(value = "/{empresaId}/{veiculoId}/entrada", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<MovimentacaoVeiculoDTO> registrarEntrada(@PathVariable Long empresaId,@PathVariable Long veiculoId) {
    	MovimentacaoVeiculoDTO movimentacao = movimentacaoVeiculoService.registrarMovimentacao(empresaId,veiculoId, TipoMovimentacao.ENTRADA);
        return ResponseEntity.ok(movimentacao);
    }

    @PostMapping(value = "/{empresaId}/{veiculoId}/saida", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<MovimentacaoVeiculoDTO> registrarSaida(@PathVariable Long empresaId, @PathVariable Long veiculoId) {
    	MovimentacaoVeiculoDTO movimentacao = movimentacaoVeiculoService.registrarMovimentacao(empresaId,veiculoId, TipoMovimentacao.SAIDA);
        return ResponseEntity.ok(movimentacao);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<MovimentacaoVeiculoDTO> listarMovimentacoes() {
        return movimentacaoVeiculoService.listarMovimentacoes();
    }
}
