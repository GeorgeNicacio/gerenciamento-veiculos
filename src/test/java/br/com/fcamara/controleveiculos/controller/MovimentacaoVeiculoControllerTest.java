package br.com.fcamara.controleveiculos.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

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

        // Execução do método do controlador, agora retornando ResponseEntity
        ResponseEntity<MovimentacaoVeiculoDTO> response = movimentacaoVeiculoController.registrarEntrada(1L, 2L);

        // Verificações
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue()); // Verifica se o status é 200 OK
        assertNotNull(response.getBody()); // Verifica se o corpo da resposta não é nulo
        assertEquals("Empresa Teste", response.getBody().getEmpresa().getNome()); // Verifica o nome da empresa
        assertEquals("Toyota", response.getBody().getVeiculo().getMarca()); // Verifica a marca do veículo
        assertEquals(TipoMovimentacao.ENTRADA, response.getBody().getTipoMovimentacao()); // Verifica o tipo de movimentação
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

        // Execução do método do controlador, agora retornando ResponseEntity
        ResponseEntity<MovimentacaoVeiculoDTO> response = movimentacaoVeiculoController.registrarSaida(1L, 2L);

        // Verificações
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue()); // Verifica se o status é 200 OK
        assertNotNull(response.getBody()); // Verifica se o corpo da resposta não é nulo
        assertEquals("Empresa Teste", response.getBody().getEmpresa().getNome()); // Verifica o nome da empresa
        assertEquals("Toyota", response.getBody().getVeiculo().getMarca()); // Verifica a marca do veículo
        assertEquals(TipoMovimentacao.SAIDA, response.getBody().getTipoMovimentacao()); // Verifica o tipo de movimentação
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
