package br.com.fcamara.controleveiculos.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.fcamara.controleveiculos.dtos.EmpresaDTO;
import br.com.fcamara.controleveiculos.model.Empresa;
import br.com.fcamara.controleveiculos.model.User;
import br.com.fcamara.controleveiculos.service.EmpresaService;

@SpringBootTest
@AutoConfigureMockMvc
class EmpresaControllerTest {

    @Mock
    private EmpresaService empresaService;

    @InjectMocks
    private EmpresaController empresaController;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssociarVeiculoComSucesso() {
        Empresa empresaMock = new Empresa();
        empresaMock.setId(1L);
        empresaMock.setCnpj("12345678000199");

        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setNome("Empresa Teste");

        // Simulando a autenticação
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("12345678000199");

        // Mockando o comportamento do serviço
        when(empresaService.buscarEmpresaPorId(1L)).thenReturn(Optional.of(empresaMock));
        when(empresaService.associarVeiculo(1L, 1L)).thenReturn(empresaDTO);

        // Agora a chamada ao controller deve retornar ResponseEntity
        ResponseEntity<EmpresaDTO> response = empresaController.associarVeiculo(1L, 1L);

        // Verificações
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue()); // Verifica se o status é 200 OK
        assertNotNull(response.getBody()); // Verifica se o corpo da resposta não é nulo
        assertEquals("Empresa Teste", response.getBody().getNome()); // Verifica o conteúdo do DTO no corpo
    }

    @Test
    void testAssociarVeiculoComErroCNPJ() {
        Empresa empresaMock = new Empresa();
        empresaMock.setId(1L);
        empresaMock.setCnpj("12345678000199");

        // Simulando a autenticação com um CNPJ diferente
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("98765432000199"); // CNPJ diferente

        // Mockando o comportamento do serviço
        when(empresaService.buscarEmpresaPorId(1L)).thenReturn(Optional.of(empresaMock));

        // Agora a chamada ao controller deve retornar ResponseEntity
        ResponseEntity<EmpresaDTO> response = empresaController.associarVeiculo(1L, 1L);

        // Verificações
        assertNotNull(response);
        assertEquals(403, response.getStatusCodeValue()); // Espera um status HTTP 403 Forbidden
        assertNull(response.getBody()); // O corpo da resposta deve ser nulo
    }

    @Test
    void testCadastrarEmpresaComSucesso() {
        Empresa input = new Empresa();
        input.setNome("Empresa Teste");
        input.setCnpj("12345678000199");
        input.setEndereco("Rua X");
        input.setTelefone("12345678");
        input.setVagasMotos(10);
        input.setVagasCarros(20);

        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setNome("Empresa Teste");

        // Mockando o comportamento do serviço
        when(empresaService.salvarEmpresa(any(Empresa.class))).thenReturn(empresaDTO);

        // Agora a chamada ao controller deve retornar ResponseEntity
        ResponseEntity<EmpresaDTO> response = empresaController.cadastrarEmpresa(input);

        // Verificações
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue()); // Verifica se o status é 200 OK
        assertNotNull(response.getBody()); // Verifica se o corpo da resposta não é nulo
        assertEquals("Empresa Teste", response.getBody().getNome()); // Verifica o nome da empresa no DTO
    }

    @Test
    void testListarEmpresas() {
        EmpresaDTO empresa1 = new EmpresaDTO();
        empresa1.setNome("Empresa 1");

        EmpresaDTO empresa2 = new EmpresaDTO();
        empresa2.setNome("Empresa 2");

        List<EmpresaDTO> empresasMock = Arrays.asList(empresa1, empresa2);

        when(empresaService.listarEmpresas()).thenReturn(empresasMock);

        List<EmpresaDTO> resultado = empresaController.listarEmpresas();

        assertEquals(2, resultado.size());
        assertEquals("Empresa 1", resultado.get(0).getNome());
        assertEquals("Empresa 2", resultado.get(1).getNome());
    }

    @Test
    void testBuscarEmpresaPorIdComSucesso() {
        Empresa empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNome("Empresa 1");

        when(empresaService.buscarEmpresaPorId(1L)).thenReturn(Optional.of(empresa));

        ResponseEntity<Empresa> resultado = empresaController.buscarEmpresaPorId(1L);

        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals("Empresa 1", resultado.getBody().getNome());
    }

    @Test
    void testBuscarEmpresaPorIdNotFound() {
        when(empresaService.buscarEmpresaPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Empresa> resultado = empresaController.buscarEmpresaPorId(1L);

        assertEquals(404, resultado.getStatusCodeValue());
    }
}