package br.com.fcamara.controleveiculos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.fcamara.controleveiculos.dtos.EmpresaDTO;
import br.com.fcamara.controleveiculos.model.Empresa;
import br.com.fcamara.controleveiculos.model.User;
import br.com.fcamara.controleveiculos.model.Veiculo;
import br.com.fcamara.controleveiculos.repository.EmpresaRepository;
import br.com.fcamara.controleveiculos.repository.UserRepository;
import br.com.fcamara.controleveiculos.repository.VeiculoRepository;
import br.com.fcamara.controleveiculos.service.impl.EmpresaServiceImpl;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceImplTest {

	@Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmpresaServiceImpl empresaService;

    private Empresa empresaMock;
    private User userMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Criação de objetos de teste
        empresaMock = new Empresa();
        empresaMock.setId(1L);
        empresaMock.setNome("Empresa Teste");
        empresaMock.setCnpj("12345678000199");
        empresaMock.setEndereco("Rua Teste");
        empresaMock.setTelefone("123456789");
        empresaMock.setVagasMotos(10);
        empresaMock.setVagasCarros(20);

        userMock = new User();
        userMock.setUsername("admin");
        userMock.setPassword("senha123");
    }

    @Test
    void testSalvarEmpresaComSucesso() {
        // Simulação do comportamento dos mocks
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresaMock);
        when(modelMapper.map(any(Empresa.class), eq(EmpresaDTO.class))).thenReturn(new EmpresaDTO());

        // Execução do método
        EmpresaDTO resultado = empresaService.salvarEmpresa(empresaMock);

        // Verificações
        assertNotNull(resultado);
        verify(empresaRepository).save(any(Empresa.class));
    }

    @Test
    void testSalvarEmpresaCNPJDuplicado() {
        // Simulação de exceção de violação de integridade ao salvar a empresa
        when(empresaRepository.save(any(Empresa.class))).thenThrow(new DataIntegrityViolationException("CNPJ já cadastrado"));

        // Execução do método e captura da exceção
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            empresaService.salvarEmpresa(empresaMock);
        });

        // Verificação
        assertEquals("CNPJ já cadastrado no sistema.", exception.getMessage());
    }

    @Test
    void testListarEmpresasComSucesso() {
        // Simulação do comportamento do repositório
        when(empresaRepository.findAll()).thenReturn(Arrays.asList(empresaMock));

        // Execução do método
        List<EmpresaDTO> resultado = empresaService.listarEmpresas();

        // Verificações
        assertEquals(1, resultado.size());
        verify(empresaRepository).findAll();
    }

    @Test
    void testBuscarEmpresaPorIdComSucesso() {
        // Simulação do comportamento do repositório
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresaMock));

        // Execução do método
        Optional<Empresa> resultado = empresaService.buscarEmpresaPorId(1L);

        // Verificações
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void testBuscarEmpresaPorIdNaoEncontrada() {
        // Simulação do comportamento do repositório retornando Optional vazio
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        // Execução do método
        Optional<Empresa> resultado = empresaService.buscarEmpresaPorId(1L);

        // Verificações
        assertFalse(resultado.isPresent());
    }

    @Test
    void testAtualizarEmpresaComSucesso() {
        Empresa empresaAtualizada = new Empresa();
        empresaAtualizada.setId(1L);  // Certifique-se de definir o ID
        empresaAtualizada.setNome("Empresa Atualizada");

        // Simulação do comportamento do repositório
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresaMock)); // Certifique-se de que o ID é o mesmo em todas as partes
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresaAtualizada);

        // Simulação do mapeamento para EmpresaDTO
        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setNome("Empresa Atualizada");
        empresaDTO.setId(1L);  // Simulação do mapeamento com o ID correto
        when(modelMapper.map(any(Empresa.class), eq(EmpresaDTO.class))).thenReturn(empresaDTO);

        // Execução do método
        EmpresaDTO resultado = empresaService.atualizarEmpresa(1L, empresaAtualizada);

        // Verificações
        assertNotNull(resultado); // Certifique-se de que o resultado não é nulo
        assertEquals(1L, resultado.getId()); // Verifique se o ID está correto
        assertEquals("Empresa Atualizada", resultado.getNome()); // Verifique se o nome foi atualizado corretamente
        verify(empresaRepository).findById(1L); // Verifique se o findById foi chamado corretamente
        verify(empresaRepository).save(any(Empresa.class)); // Verifique se o método save foi chamado
    }

    @Test
    void testAtualizarEmpresaNaoEncontrada() {
        // Simulação do repositório retornando Optional vazio
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        // Execução do método e captura da exceção
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            empresaService.atualizarEmpresa(1L, empresaMock);
        });

        // Verificações
        assertEquals("Empresa não encontrada com id 1", exception.getMessage());
    }

    @Test
    void testDeletarEmpresaComSucesso() {
        // Simulação do comportamento do repositório
        doNothing().when(empresaRepository).deleteById(1L);

        // Execução do método
        empresaService.deletarEmpresa(1L);

        // Verificações
        verify(empresaRepository).deleteById(1L);
    }

    @Test
    void testAssociarVeiculoComSucesso() {
        Veiculo veiculoMock = new Veiculo();
        veiculoMock.setId(1L);
        veiculoMock.setMarca("Toyota");

        // Simulação do comportamento do repositório
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresaMock));
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoMock));

        // Simula o salvamento da empresa com o veículo associado
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresaMock);

        // Simula o mapeamento para EmpresaDTO
        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setNome("Empresa Teste");
        when(modelMapper.map(any(Empresa.class), eq(EmpresaDTO.class))).thenReturn(empresaDTO);

        // Execução do método
        EmpresaDTO resultado = empresaService.associarVeiculo(1L, 1L);

        // Verificações
        assertNotNull(resultado, "O resultado não deve ser nulo"); // Verifica se o resultado não é nulo
        verify(empresaRepository).findById(1L); // Verifica se findById foi chamado
        verify(veiculoRepository).findById(1L); // Verifica se findById foi chamado
        verify(empresaRepository).save(any(Empresa.class)); // Verifica se a empresa foi salva
    }

    @Test
    void testAssociarVeiculoEmpresaOuVeiculoNaoEncontrado() {
        // Simulação do comportamento do repositório retornando Optional vazio para empresa e veículo
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());
        when(veiculoRepository.findById(1L)).thenReturn(Optional.empty());

        // Execução do método e captura da exceção
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            empresaService.associarVeiculo(1L, 1L);
        });

        // Verificações
        assertEquals("Empresa ou veículo não encontrado", exception.getMessage());
    }
}