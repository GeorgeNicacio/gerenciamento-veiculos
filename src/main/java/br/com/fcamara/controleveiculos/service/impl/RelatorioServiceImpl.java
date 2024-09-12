package br.com.fcamara.controleveiculos.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fcamara.controleveiculos.dtos.MovimentacaoVeiculoDTO;
import br.com.fcamara.controleveiculos.model.MovimentacaoVeiculo;
import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;
import br.com.fcamara.controleveiculos.repository.MovimentacaoVeiculoRepository;
import br.com.fcamara.controleveiculos.service.RelatorioService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RelatorioServiceImpl implements RelatorioService {

    @Autowired
    private MovimentacaoVeiculoRepository movimentacaoVeiculoRepository;
    
    private final ModelMapper modelMapper;
    
    // Quantidade de entradas por período
    public long contarEntradasPorPeriodo(Long empresaId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return movimentacaoVeiculoRepository.countMovimentacoesByTipoAndPeriodo(empresaId, TipoMovimentacao.ENTRADA, dataInicio, dataFim);
    }

    // Quantidade de saídas por período
    public long contarSaidasPorPeriodo(Long empresaId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return movimentacaoVeiculoRepository.countMovimentacoesByTipoAndPeriodo(empresaId, TipoMovimentacao.SAIDA, dataInicio, dataFim);
    }
    
    // Relatório de movimentações por empresa e período
    public List<MovimentacaoVeiculoDTO> gerarRelatorioMovimentacao(Long empresaId, LocalDateTime dataInicio, LocalDateTime dataFim) {
    	List<MovimentacaoVeiculo> list = movimentacaoVeiculoRepository.findAllByEmpresaAndPeriodo(empresaId, dataInicio, dataFim); 
        // Converte a lista de Veiculo para VeiculoDTO
        return list.stream()
                       .map(this::convertToDto)
                       .collect(Collectors.toList());
    }
    
    // Relatório de movimentações por empresa e período
    public List<Object[]> gerarRelatorioMovimentacaoPorHora(Long empresaId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return movimentacaoVeiculoRepository.findMovimentacoesAgrupadasPorHora(empresaId, dataInicio, dataFim);
    }
    
 // Método de conversão
    private MovimentacaoVeiculoDTO convertToDto(MovimentacaoVeiculo movimentacaoVeiculo) {
        return modelMapper.map(movimentacaoVeiculo, MovimentacaoVeiculoDTO.class); 
    }

}