package br.com.fcamara.controleveiculos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import br.com.fcamara.controleveiculos.dtos.VeiculoDTO;
import br.com.fcamara.controleveiculos.model.Empresa;
import br.com.fcamara.controleveiculos.model.Veiculo;
import br.com.fcamara.controleveiculos.repository.EmpresaRepository;
import br.com.fcamara.controleveiculos.repository.VeiculoRepository;
import br.com.fcamara.controleveiculos.service.impl.EmpresaServiceImpl;
import br.com.fcamara.controleveiculos.service.impl.VeiculoServiceImpl;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;
    
    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private VeiculoServiceImpl veiculoService;
    
    @InjectMocks
    private EmpresaServiceImpl empresaService;

    @Mock
    private Veiculo veiculoMock;

    @BeforeEach
    void setUp() {
    	 // Inicializa os mocks do Mockito
        MockitoAnnotations.openMocks(this);
        
        veiculoMock = new Veiculo();
        veiculoMock.setId(1L);
        veiculoMock.setMarca("Toyota");
        veiculoMock.setModelo("Corolla");
        veiculoMock.setCor("Preto");
        veiculoMock.setPlaca("ABC-1234");
    }

    @Test
    void testSalvarVeiculoComSucesso() {
        // Simulação do comportamento do repositório e do mapeamento
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculoMock);
        when(modelMapper.map(any(Veiculo.class), eq(VeiculoDTO.class))).thenReturn(new VeiculoDTO());

        // Execução do método
        VeiculoDTO resultado = veiculoService.salvarVeiculo(veiculoMock);

        // Verificações
        assertNotNull(resultado);
        verify(veiculoRepository).save(any(Veiculo.class));
        verify(modelMapper).map(any(Veiculo.class), eq(VeiculoDTO.class));
    }

    @Test
    void testListarVeiculos() {
        // Simulação do comportamento do repositório
        when(veiculoRepository.findAll()).thenReturn(Arrays.asList(veiculoMock));

        // Execução do método
        List<VeiculoDTO> resultado = veiculoService.listarVeiculos();

        // Verificações
        assertEquals(1, resultado.size());
        verify(veiculoRepository).findAll();
    }

    @Test
    void testBuscarVeiculoPorIdComSucesso() {
        // Simulação do comportamento do repositório
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoMock));

        // Execução do método
        Optional<Veiculo> resultado = veiculoService.buscarVeiculoPorId(1L);

        // Verificações
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(veiculoRepository).findById(1L);
    }

    @Test
    void testBuscarVeiculoPorIdNaoEncontrado() {
        // Simulação do comportamento do repositório
        when(veiculoRepository.findById(1L)).thenReturn(Optional.empty());

        // Execução do método
        Optional<Veiculo> resultado = veiculoService.buscarVeiculoPorId(1L);

        // Verificações
        assertFalse(resultado.isPresent());
        verify(veiculoRepository).findById(1L);
    }

    @Test
    void testAtualizarVeiculoComSucesso() {
        // Criação do mock do veículo atualizado
        Veiculo veiculoAtualizado = new Veiculo();
        veiculoAtualizado.setMarca("Honda");
        veiculoAtualizado.setModelo("Civic");

        // Simulação do comportamento do repositório
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoMock));
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculoAtualizado);
        when(modelMapper.map(any(Veiculo.class), eq(VeiculoDTO.class))).thenReturn(new VeiculoDTO());

        // Execução do método
        VeiculoDTO resultado = veiculoService.atualizarVeiculo(1L, veiculoAtualizado);

        // Verificações
        assertNotNull(resultado);
        verify(veiculoRepository).findById(1L);
        verify(veiculoRepository).save(any(Veiculo.class));
    }

    @Test
    void testDeletarVeiculoComSucesso() {
        // Simulação do comportamento do repositório
        doNothing().when(veiculoRepository).deleteById(1L);

        // Execução do método
        veiculoService.deletarVeiculo(1L);

        // Verificações
        verify(veiculoRepository).deleteById(1L);
    }

    @Test
    void testCadastrarVeiculoComEmpresasComSucesso() {
        // Mock de comportamento do veículo
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculoMock);
        when(modelMapper.map(any(Veiculo.class), eq(VeiculoDTO.class))).thenReturn(new VeiculoDTO());

        // Mock de comportamento das empresas (supondo que você tenha um EmpresaRepository)
        Empresa empresa1 = new Empresa();
        empresa1.setId(1L);
        empresa1.setNome("Empresa 1");
        
        Empresa empresa2 = new Empresa();
        empresa2.setId(2L);
        empresa2.setNome("Empresa 2");

        // Simulação de retorno das empresas associadas
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa1));
        when(empresaRepository.findById(2L)).thenReturn(Optional.of(empresa2));

        // Conjunto de IDs de empresas simulados
        Set<Long> empresaIds = Set.of(1L, 2L);

        // Execução do método
        VeiculoDTO resultado = veiculoService.cadastrarVeiculo(veiculoMock, empresaIds);

        // Verificações
        assertNotNull(resultado);
        verify(veiculoRepository).save(any(Veiculo.class));
        verify(empresaRepository).findById(1L);  // Verifica se o repositório buscou a empresa com ID 1
        verify(empresaRepository).findById(2L);  // Verifica se o repositório buscou a empresa com ID 2
        verify(modelMapper).map(any(Veiculo.class), eq(VeiculoDTO.class));
    }
}
