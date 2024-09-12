package br.com.fcamara.controleveiculos.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import br.com.fcamara.controleveiculos.dtos.MovimentacaoVeiculoDTO;
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
        // Criação de DTOs de movimentações
        MovimentacaoVeiculoDTO mov1 = new MovimentacaoVeiculoDTO();
        mov1.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        MovimentacaoVeiculoDTO mov2 = new MovimentacaoVeiculoDTO();
        mov2.setTipoMovimentacao(TipoMovimentacao.SAIDA);

        // Simulação do serviço
        when(relatorioService.gerarRelatorioMovimentacao(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(mov1, mov2));

        // Execução do método do controlador
        ResponseEntity<List<MovimentacaoVeiculoDTO>> response = relatorioController.gerarRelatorioMovimentacao(1L, 
            LocalDateTime.of(2023, 9, 1, 0, 0), LocalDateTime.of(2023, 9, 30, 23, 59));

        // Verificações
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(TipoMovimentacao.ENTRADA, response.getBody().get(0).getTipoMovimentacao());
        assertEquals(TipoMovimentacao.SAIDA, response.getBody().get(1).getTipoMovimentacao());
    }


    @Test
    void testGerarRelatorioMovimentacaoPorHoraComSucesso() {
        // Simulação dos resultados do serviço (hora, tipoMovimentacao, quantidade)
        Object[] hora1 = new Object[]{9, TipoMovimentacao.ENTRADA, 5L};
        Object[] hora2 = new Object[]{10, TipoMovimentacao.SAIDA, 3L};

        // Simulação do serviço
        when(relatorioService.gerarRelatorioMovimentacaoPorHora(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(hora1, hora2));

        // Execução do método do controlador
        ResponseEntity<List<Object[]>> response = relatorioController.gerarRelatorioMovimentacaoPorHora(1L, 
            LocalDateTime.of(2023, 9, 1, 0, 0), LocalDateTime.of(2023, 9, 30, 23, 59));

        // Verificações
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(9, response.getBody().get(0)[0]);
        assertEquals(TipoMovimentacao.ENTRADA, response.getBody().get(0)[1]);
        assertEquals(5L, response.getBody().get(0)[2]);

        assertEquals(10, response.getBody().get(1)[0]);
        assertEquals(TipoMovimentacao.SAIDA, response.getBody().get(1)[1]);
        assertEquals(3L, response.getBody().get(1)[2]);
    }


    @Test
    void testObterSumarioEntradaSaidaComSucesso() {
        // Simulação do serviço
        when(relatorioService.contarEntradasPorPeriodo(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(10L);
        when(relatorioService.contarSaidasPorPeriodo(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(8L);

        // Execução do método do controlador
        ResponseEntity<Map<String, Long>> response = relatorioController.obterSumarioEntradaSaida(1L, 
            LocalDateTime.of(2023, 9, 1, 0, 0), LocalDateTime.of(2023, 9, 30, 23, 59));

        // Verificações
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().get("entradas"));
        assertEquals(8L, response.getBody().get("saidas"));
    }

}
