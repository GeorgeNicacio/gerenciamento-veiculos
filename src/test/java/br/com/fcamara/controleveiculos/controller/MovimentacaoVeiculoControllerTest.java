package br.com.fcamara.controleveiculos.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.fcamara.controleveiculos.dtos.EmpresaDTO;
import br.com.fcamara.controleveiculos.dtos.MovimentacaoVeiculoDTO;
import br.com.fcamara.controleveiculos.dtos.VeiculoDTO;
import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;
import br.com.fcamara.controleveiculos.service.MovimentacaoVeiculoService;

@SpringBootTest
@AutoConfigureMockMvc
class MovimentacaoVeiculoControllerTest {

    @Mock
    private MovimentacaoVeiculoService movimentacaoVeiculoService;

    @InjectMocks
    private MovimentacaoVeiculoController movimentacaoVeiculoController;

    private EmpresaDTO empresaMock;
    private VeiculoDTO veiculoMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Criação de objetos de teste (EmpresaDTO e VeiculoDTO)
        empresaMock = new EmpresaDTO();
        empresaMock.setId(1L);
        empresaMock.setNome("Empresa Teste");

        veiculoMock = new VeiculoDTO();
        veiculoMock.setMarca("Toyota");
        veiculoMock.setModelo("Corolla");
    }

    @Test
    void testRegistrarEntradaComSucesso() {
        // Criação do DTO de movimentação com objetos completos de EmpresaDTO e VeiculoDTO
        MovimentacaoVeiculoDTO movimentacaoDTO = new MovimentacaoVeiculoDTO();
        movimentacaoDTO.setEmpresa(empresaMock);
        movimentacaoDTO.setVeiculo(veiculoMock);
        movimentacaoDTO.setTipoMovimentacao(TipoMovimentacao.ENTRADA);

        // Simulação do serviço
        when(movimentacaoVeiculoService.registrarMovimentacao(1L, 2L, TipoMovimentacao.ENTRADA)).thenReturn(movimentacaoDTO);

        // Execução do método do controlador
        MovimentacaoVeiculoDTO resultado = movimentacaoVeiculoController.registrarEntrada(1L, 2L);

        // Verificações
        assertNotNull(resultado);
        assertEquals("Empresa Teste", resultado.getEmpresa().getNome());
        assertEquals("Toyota", resultado.getVeiculo().getMarca());
        assertEquals(TipoMovimentacao.ENTRADA, resultado.getTipoMovimentacao());
    }

    @Test
    void testRegistrarSaidaComSucesso() {
        // Criação do DTO de movimentação com objetos completos de EmpresaDTO e VeiculoDTO
        MovimentacaoVeiculoDTO movimentacaoDTO = new MovimentacaoVeiculoDTO();
        movimentacaoDTO.setEmpresa(empresaMock);
        movimentacaoDTO.setVeiculo(veiculoMock);
        movimentacaoDTO.setTipoMovimentacao(TipoMovimentacao.SAIDA);

        // Simulação do serviço
        when(movimentacaoVeiculoService.registrarMovimentacao(1L, 2L, TipoMovimentacao.SAIDA)).thenReturn(movimentacaoDTO);

        // Execução do método do controlador
        MovimentacaoVeiculoDTO resultado = movimentacaoVeiculoController.registrarSaida(1L, 2L);

        // Verificações
        assertNotNull(resultado);
        assertEquals("Empresa Teste", resultado.getEmpresa().getNome());
        assertEquals("Toyota", resultado.getVeiculo().getMarca());
        assertEquals(TipoMovimentacao.SAIDA, resultado.getTipoMovimentacao());
    }

    @Test
    void testListarMovimentacoesComSucesso() {
        // Criação de DTOs fictícios de movimentação com objetos completos de EmpresaDTO e VeiculoDTO
        MovimentacaoVeiculoDTO movimentacao1 = new MovimentacaoVeiculoDTO();
        movimentacao1.setEmpresa(empresaMock);
        movimentacao1.setVeiculo(veiculoMock);
        movimentacao1.setTipoMovimentacao(TipoMovimentacao.ENTRADA);

        EmpresaDTO outraEmpresa = new EmpresaDTO();
        outraEmpresa.setId(3L);
        outraEmpresa.setNome("Outra Empresa");

        VeiculoDTO outroVeiculo = new VeiculoDTO();
        outroVeiculo.setMarca("Ford");
        outroVeiculo.setModelo("Focus");

        MovimentacaoVeiculoDTO movimentacao2 = new MovimentacaoVeiculoDTO();
        movimentacao2.setEmpresa(outraEmpresa);
        movimentacao2.setVeiculo(outroVeiculo);
        movimentacao2.setTipoMovimentacao(TipoMovimentacao.SAIDA);

        // Simulação do serviço
        when(movimentacaoVeiculoService.listarMovimentacoes()).thenReturn(Arrays.asList(movimentacao1, movimentacao2));

        // Execução do método do controlador
        List<MovimentacaoVeiculoDTO> resultado = movimentacaoVeiculoController.listarMovimentacoes();

        // Verificações
        assertEquals(2, resultado.size());
        assertEquals("Empresa Teste", resultado.get(0).getEmpresa().getNome());
        assertEquals("Toyota", resultado.get(0).getVeiculo().getMarca());
        assertEquals(TipoMovimentacao.ENTRADA, resultado.get(0).getTipoMovimentacao());

        assertEquals("Outra Empresa", resultado.get(1).getEmpresa().getNome());
        assertEquals("Ford", resultado.get(1).getVeiculo().getMarca());
        assertEquals(TipoMovimentacao.SAIDA, resultado.get(1).getTipoMovimentacao());
    }
}
