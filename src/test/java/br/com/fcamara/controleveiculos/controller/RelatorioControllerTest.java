package br.com.fcamara.controleveiculos.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.fcamara.controleveiculos.dtos.MovimentacaoVeiculoDTO;
import br.com.fcamara.controleveiculos.dtos.RelatorioHoraDTO;
import br.com.fcamara.controleveiculos.dtos.RelatorioInput;
import br.com.fcamara.controleveiculos.dtos.RelatorioSumarioDTO;
import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;
import br.com.fcamara.controleveiculos.service.RelatorioService;

@SpringBootTest
@AutoConfigureMockMvc
class RelatorioControllerTest {

    @Mock
    private RelatorioService relatorioService;

    @InjectMocks
    private RelatorioController relatorioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGerarRelatorioMovimentacaoComSucesso() {
        // Criação do input
        RelatorioInput input = new RelatorioInput();
        input.setEmpresaId(1L);
        input.setDataInicio(LocalDateTime.of(2023, 9, 1, 0, 0));
        input.setDataFim(LocalDateTime.of(2023, 9, 30, 23, 59));

        // Criação de DTOs de movimentações
        MovimentacaoVeiculoDTO mov1 = new MovimentacaoVeiculoDTO();
        mov1.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        MovimentacaoVeiculoDTO mov2 = new MovimentacaoVeiculoDTO();
        mov2.setTipoMovimentacao(TipoMovimentacao.SAIDA);

        // Simulação do serviço
        when(relatorioService.gerarRelatorioMovimentacao(1L, input.getDataInicio(), input.getDataFim()))
                .thenReturn(Arrays.asList(mov1, mov2));

        // Execução do método do controlador
        List<MovimentacaoVeiculoDTO> resultado = relatorioController.gerarRelatorioMovimentacao(input);

        // Verificações
        assertEquals(2, resultado.size());
        assertEquals(TipoMovimentacao.ENTRADA, resultado.get(0).getTipoMovimentacao());
        assertEquals(TipoMovimentacao.SAIDA, resultado.get(1).getTipoMovimentacao());
    }

    @Test
    void testGerarRelatorioMovimentacaoPorHoraComSucesso() {
        // Criação do input
        RelatorioInput input = new RelatorioInput();
        input.setEmpresaId(1L);
        input.setDataInicio(LocalDateTime.of(2023, 9, 1, 0, 0));
        input.setDataFim(LocalDateTime.of(2023, 9, 30, 23, 59));

        // Simulação dos resultados do serviço (hora, tipoMovimentacao, quantidade)
        Object[] hora1 = new Object[]{9, TipoMovimentacao.ENTRADA, 5L};
        Object[] hora2 = new Object[]{10, TipoMovimentacao.SAIDA, 3L};

        // Simulação do serviço
        when(relatorioService.gerarRelatorioMovimentacaoPorHora(1L, input.getDataInicio(), input.getDataFim()))
                .thenReturn(Arrays.asList(hora1, hora2));

        // Execução do método do controlador
        List<RelatorioHoraDTO> resultado = relatorioController.gerarRelatorioMovimentacaoPorHora(input);

        // Verificações
        assertEquals(2, resultado.size());
        assertEquals(9, resultado.get(0).getHora());
        assertEquals(TipoMovimentacao.ENTRADA, resultado.get(0).getTipo());
        assertEquals(5L, resultado.get(0).getQtd());

        assertEquals(10, resultado.get(1).getHora());
        assertEquals(TipoMovimentacao.SAIDA, resultado.get(1).getTipo());
        assertEquals(3L, resultado.get(1).getQtd());
    }

    @Test
    void testObterSumarioEntradaSaidaComSucesso() {
        // Criação do input
        RelatorioInput input = new RelatorioInput();
        input.setEmpresaId(1L);
        input.setDataInicio(LocalDateTime.of(2023, 9, 1, 0, 0));
        input.setDataFim(LocalDateTime.of(2023, 9, 30, 23, 59));

        // Simulação do serviço
        when(relatorioService.contarEntradasPorPeriodo(1L, input.getDataInicio(), input.getDataFim())).thenReturn(10L);
        when(relatorioService.contarSaidasPorPeriodo(1L, input.getDataInicio(), input.getDataFim())).thenReturn(8L);

        // Execução do método do controlador
        RelatorioSumarioDTO resultado = relatorioController.obterSumarioEntradaSaida(input);

        // Verificações
        assertNotNull(resultado);
        assertEquals(10L, resultado.getEntradas());
        assertEquals(8L, resultado.getSaidas());
    }
}
