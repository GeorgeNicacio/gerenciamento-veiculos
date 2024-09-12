package br.com.fcamara.controleveiculos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import br.com.fcamara.controleveiculos.dtos.MovimentacaoVeiculoDTO;
import br.com.fcamara.controleveiculos.model.Empresa;
import br.com.fcamara.controleveiculos.model.MovimentacaoVeiculo;
import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;
import br.com.fcamara.controleveiculos.repository.MovimentacaoVeiculoRepository;
import br.com.fcamara.controleveiculos.service.impl.RelatorioServiceImpl;

class RelatorioServiceTest {

    @Mock
    private MovimentacaoVeiculoRepository movimentacaoVeiculoRepository;

    @InjectMocks
    private RelatorioServiceImpl relatorioServiceImpl;  // Implementação a ser testada
    
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testGerarRelatorioMovimentacao() {
        // Configurando dados de teste
        Long empresaId = 1L;
        LocalDateTime dataInicio = LocalDateTime.of(2023, 9, 10, 0, 0);
        LocalDateTime dataFim = LocalDateTime.of(2023, 9, 12, 23, 59);

        // Simulando o comportamento do repositório para retornar entidades
        MovimentacaoVeiculo movimentacao1 = new MovimentacaoVeiculo();
        movimentacao1.setEmpresa(new Empresa());
        movimentacao1.setDataHora(dataInicio);
        
        MovimentacaoVeiculo movimentacao2 = new MovimentacaoVeiculo();
        movimentacao2.setEmpresa(new Empresa());
        movimentacao2.setDataHora(dataFim);
        
        List<MovimentacaoVeiculo> movimentacoesMock = Arrays.asList(movimentacao1, movimentacao2);

        // Simulação do comportamento do método do repositório
        when(movimentacaoVeiculoRepository.findAllByEmpresaAndPeriodo(any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(movimentacoesMock);

        // Simulação de retorno do ModelMapper para converter as entidades em DTOs
        MovimentacaoVeiculoDTO movimentacaoDTO1 = new MovimentacaoVeiculoDTO();
        MovimentacaoVeiculoDTO movimentacaoDTO2 = new MovimentacaoVeiculoDTO();
        
        when(modelMapper.map(any(MovimentacaoVeiculo.class), eq(MovimentacaoVeiculoDTO.class)))
                .thenReturn(movimentacaoDTO1, movimentacaoDTO2);

        // Execução do método
        List<MovimentacaoVeiculoDTO> resultado = relatorioServiceImpl.gerarRelatorioMovimentacao(empresaId, dataInicio, dataFim);

        // Verificações
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }


    @Test
    void testGerarRelatorioMovimentacaoPorHora() {
        // Configurando dados de teste
        Long empresaId = 1L;
        LocalDateTime dataInicio = LocalDateTime.of(2023, 9, 10, 0, 0);
        LocalDateTime dataFim = LocalDateTime.of(2023, 9, 12, 23, 59);

        // Simulando o retorno de objetos de movimentação por hora
        Object[] entradaHora1 = { 8, TipoMovimentacao.ENTRADA, 10L }; // Hora, Tipo, Contagem
        Object[] entradaHora2 = { 9, TipoMovimentacao.SAIDA, 5L };
        List<Object[]> movimentacaoPorHoraMock = Arrays.asList(entradaHora1, entradaHora2);

        // Simulação do comportamento do método do repositório
        when(movimentacaoVeiculoRepository.findMovimentacoesAgrupadasPorHora(any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(movimentacaoPorHoraMock);

        // Execução do método
        List<Object[]> resultado = relatorioServiceImpl.gerarRelatorioMovimentacaoPorHora(empresaId, dataInicio, dataFim);

        // Verificações
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(TipoMovimentacao.ENTRADA, resultado.get(0)[1]);  // Verificando o tipo de movimentação
        assertEquals(10L, resultado.get(0)[2]);  // Verificando o número de entradas
    }


    @Test
    void testContarEntradasPorPeriodo() {
        // Configurando dados de teste
        Long empresaId = 1L;
        LocalDateTime dataInicio = LocalDateTime.of(2023, 9, 10, 0, 0);
        LocalDateTime dataFim = LocalDateTime.of(2023, 9, 12, 23, 59);

        // Simulação do comportamento do método no repositório
        when(movimentacaoVeiculoRepository.countMovimentacoesByTipoAndPeriodo(
                anyLong(), eq(TipoMovimentacao.ENTRADA), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(15L);

        // Execução do método
        long entradas = relatorioServiceImpl.contarEntradasPorPeriodo(empresaId, dataInicio, dataFim);

        // Verificação
        assertEquals(15L, entradas);
    }

    @Test
    void testContarSaidasPorPeriodo() {
        // Configurando dados de teste
        Long empresaId = 1L;
        LocalDateTime dataInicio = LocalDateTime.of(2023, 9, 10, 0, 0);
        LocalDateTime dataFim = LocalDateTime.of(2023, 9, 12, 23, 59);

        // Simulação do comportamento do método no repositório
        when(movimentacaoVeiculoRepository.countMovimentacoesByTipoAndPeriodo(
                anyLong(), eq(TipoMovimentacao.SAIDA), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(12L);

        // Execução do método
        long saidas = relatorioServiceImpl.contarSaidasPorPeriodo(empresaId, dataInicio, dataFim);

        // Verificação
        assertEquals(12L, saidas);
    }
}