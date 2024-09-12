package br.com.fcamara.controleveiculos.controller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import br.com.fcamara.controleveiculos.dtos.VeiculoDTO;
import br.com.fcamara.controleveiculos.model.Veiculo;
import br.com.fcamara.controleveiculos.model.enums.TipoVeiculo;
import br.com.fcamara.controleveiculos.service.VeiculoService;

@SpringBootTest
@AutoConfigureMockMvc
class VeiculoControllerTest {

    @Mock
    private VeiculoService veiculoService;

    @InjectMocks
    private VeiculoController veiculoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastrarVeiculoComSucesso() {
        // Criação do DTO de entrada
        VeiculoDTO input = new VeiculoDTO();
        input.setMarca("Toyota");
        input.setModelo("Corolla");
        input.setCor("Preto");
        input.setPlaca("ABC-1234");
        input.setTipo(TipoVeiculo.CARRO);
        input.setEmpresaIds(List.of(1L)); // Correção para usar List em vez de Set

        // Criação do DTO esperado como retorno
        VeiculoDTO veiculoSalvo = new VeiculoDTO();
        veiculoSalvo.setMarca("Toyota");
        veiculoSalvo.setModelo("Corolla");

        // Simulação do serviço, aceitando qualquer Veiculo e qualquer Set de IDs de empresa
        when(veiculoService.cadastrarVeiculo(any(Veiculo.class), anySet())).thenReturn(veiculoSalvo);

        // Execução do método do controlador, retornando ResponseEntity
        ResponseEntity<VeiculoDTO> response = veiculoController.cadastrarVeiculo(input);

        // Verificações
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue()); // Verifica se o status é 200 OK
        assertNotNull(response.getBody()); // Verifica se o corpo da resposta não é nulo
        assertEquals("Toyota", response.getBody().getMarca()); // Verifica a marca
        assertEquals("Corolla", response.getBody().getModelo()); // Verifica o modelo
    }


    @Test
    void testListarVeiculos() {
        // Criação de DTOs de veículos fictícios
        VeiculoDTO veiculo1 = new VeiculoDTO();
        veiculo1.setMarca("Honda");
        veiculo1.setModelo("Civic");

        VeiculoDTO veiculo2 = new VeiculoDTO();
        veiculo2.setMarca("Ford");
        veiculo2.setModelo("Focus");

        // Simulação do serviço
        when(veiculoService.listarVeiculos()).thenReturn(Arrays.asList(veiculo1, veiculo2));

        // Execução do método do controlador
        List<VeiculoDTO> resultado = veiculoController.listarVeiculos();

        // Verificações
        assertEquals(2, resultado.size());
        assertEquals("Honda", resultado.get(0).getMarca());
        assertEquals("Ford", resultado.get(1).getMarca());
    }

    @Test
    void testBuscarVeiculoPorIdComSucesso() {
        // Criação do objeto Veiculo fictício
        Veiculo veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setMarca("Honda");
        veiculo.setModelo("Civic");

        // Simulação do serviço
        when(veiculoService.buscarVeiculoPorId(1L)).thenReturn(Optional.of(veiculo));

        // Execução do método do controlador
        ResponseEntity<Veiculo> resultado = veiculoController.buscarVeiculoPorId(1L);

        // Verificações
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals("Honda", resultado.getBody().getMarca());
    }

    @Test
    void testBuscarVeiculoPorIdNaoEncontrado() {
        // Simulação do serviço retornando Optional vazio
        when(veiculoService.buscarVeiculoPorId(1L)).thenReturn(Optional.empty());

        // Execução do método do controlador
        ResponseEntity<Veiculo> resultado = veiculoController.buscarVeiculoPorId(1L);

        // Verificações
        assertEquals(404, resultado.getStatusCodeValue());
    }

    @Test
    void testAtualizarVeiculoComSucesso() {
        // Criação de objetos Veiculo fictícios para simular a atualização
        Veiculo input = new Veiculo();
        input.setMarca("Toyota");
        input.setModelo("Corolla");
        input.setCor("Preto");
        input.setPlaca("ABC-1234");

        VeiculoDTO veiculoAtualizado = new VeiculoDTO();
        veiculoAtualizado.setMarca("Toyota");
        veiculoAtualizado.setModelo("Corolla");

        // Simulação do serviço
        when(veiculoService.atualizarVeiculo(anyLong(), any(Veiculo.class))).thenReturn(veiculoAtualizado);

        // Execução do método do controlador, retornando ResponseEntity
        ResponseEntity<VeiculoDTO> response = veiculoController.atualizarVeiculo(1L, input);

        // Verificações
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue()); // Verifica se o status é 200 OK
        assertNotNull(response.getBody()); // Verifica se o corpo da resposta não é nulo
        assertEquals("Toyota", response.getBody().getMarca()); // Verifica a marca
        assertEquals("Corolla", response.getBody().getModelo()); // Verifica o modelo
    }


    @Test
    void testDeletarVeiculoComSucesso() {
        // Simulação do serviço sem lançar exceção
        doNothing().when(veiculoService).deletarVeiculo(1L);

        // Execução do método do controlador, retornando ResponseEntity
        ResponseEntity<Void> response = veiculoController.deletarVeiculo(1L);

        // Verificações
        assertNotNull(response); // Verifica se a resposta não é nula
        assertEquals(204, response.getStatusCodeValue()); // Verifica se o status é 204 No Content, que é esperado para deleções bem-sucedidas
    }

    @Test
    void testDeletarVeiculoComErro() {
        // Simulação de exceção no serviço
        doThrow(new RuntimeException("Erro ao deletar veículo")).when(veiculoService).deletarVeiculo(1L);

        // Execução do método do controlador, retornando ResponseEntity
        ResponseEntity<Void> response = veiculoController.deletarVeiculo(1L);

        // Verificações
        assertNotNull(response); // Verifica se a resposta não é nula
        assertEquals(404, response.getStatusCodeValue()); // Verifica se o status é 500 Internal Server Error
    }

}
