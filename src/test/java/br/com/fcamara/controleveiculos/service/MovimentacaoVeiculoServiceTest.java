package br.com.fcamara.controleveiculos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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

import br.com.fcamara.controleveiculos.dtos.EmpresaDTO;
import br.com.fcamara.controleveiculos.dtos.MovimentacaoVeiculoDTO;
import br.com.fcamara.controleveiculos.dtos.VeiculoDTO;
import br.com.fcamara.controleveiculos.model.Empresa;
import br.com.fcamara.controleveiculos.model.MovimentacaoVeiculo;
import br.com.fcamara.controleveiculos.model.Veiculo;
import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;
import br.com.fcamara.controleveiculos.model.enums.TipoVeiculo;
import br.com.fcamara.controleveiculos.repository.EmpresaRepository;
import br.com.fcamara.controleveiculos.repository.MovimentacaoVeiculoRepository;
import br.com.fcamara.controleveiculos.repository.VeiculoRepository;
import br.com.fcamara.controleveiculos.service.impl.MovimentacaoVeiculoServiceImpl;

@ExtendWith(MockitoExtension.class)
class MovimentacaoVeiculoServiceTest {

    @Mock
    private MovimentacaoVeiculoRepository movimentacaoVeiculoRepository;
    
    @Mock
    private VeiculoRepository veiculoRepository;
    
    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MovimentacaoVeiculoServiceImpl movimentacaoVeiculoService;

    @Mock
    private MovimentacaoVeiculo movimentacaoEntradaMock;
    @Mock
    private MovimentacaoVeiculo movimentacaoSaidaMock;
    @Mock
    private MovimentacaoVeiculoDTO movimentacaoEntradaDTOMock;
    @Mock
    private MovimentacaoVeiculoDTO movimentacaoSaidaDTOMock;
    @Mock
    private Empresa empresaMock;
    @Mock
    private Veiculo veiculoMock;

    @BeforeEach
    void setUp() {
        // Inicializa os mocks do Mockito
        MockitoAnnotations.openMocks(this);

        // Criação de objetos de teste (entidades e DTOs)
        empresaMock = new Empresa();
        empresaMock.setId(1L);
        empresaMock.setNome("Empresa Teste");

        veiculoMock = new Veiculo();
        veiculoMock.setId(2L);
        veiculoMock.setMarca("Toyota");
        veiculoMock.setModelo("Corolla");

        movimentacaoEntradaMock = new MovimentacaoVeiculo();
        movimentacaoEntradaMock.setEmpresa(empresaMock);
        movimentacaoEntradaMock.setVeiculo(veiculoMock);
        movimentacaoEntradaMock.setTipoMovimentacao(TipoMovimentacao.ENTRADA);

        movimentacaoSaidaMock = new MovimentacaoVeiculo();
        movimentacaoSaidaMock.setEmpresa(empresaMock);
        movimentacaoSaidaMock.setVeiculo(veiculoMock);
        movimentacaoSaidaMock.setTipoMovimentacao(TipoMovimentacao.SAIDA);

        // Criação dos DTOs
        movimentacaoEntradaDTOMock = new MovimentacaoVeiculoDTO();
        movimentacaoEntradaDTOMock.setEmpresa(new EmpresaDTO());
        movimentacaoEntradaDTOMock.setVeiculo(new VeiculoDTO());
        movimentacaoEntradaDTOMock.setTipoMovimentacao(TipoMovimentacao.ENTRADA);

        movimentacaoSaidaDTOMock = new MovimentacaoVeiculoDTO();
        movimentacaoSaidaDTOMock.setEmpresa(new EmpresaDTO());
        movimentacaoSaidaDTOMock.setVeiculo(new VeiculoDTO());
        movimentacaoSaidaDTOMock.setTipoMovimentacao(TipoMovimentacao.SAIDA);
    }

    @Test
    void testRegistrarMovimentacaoEntradaComSucesso() {
        // Criação de mocks para empresa e veículo
        Empresa empresaMock = new Empresa();
        empresaMock.setId(1L);
        empresaMock.setNome("Empresa Teste");
        empresaMock.setVagasCarros(10); // Defina o número de vagas de carros
        empresaMock.setVagasMotos(5);   // Defina o número de vagas de motos

        Veiculo veiculoMock = new Veiculo();
        veiculoMock.setId(2L);
        veiculoMock.setMarca("Toyota");
        veiculoMock.setTipo(TipoVeiculo.CARRO);  // Defina o tipo do veículo

        // Simulação do comportamento do repositório de empresa e veículo
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresaMock));
        when(veiculoRepository.findById(2L)).thenReturn(Optional.of(veiculoMock));

        // Simulação do comportamento do repositório de movimentação
        when(movimentacaoVeiculoRepository.save(any(MovimentacaoVeiculo.class))).thenReturn(movimentacaoEntradaMock);
        when(modelMapper.map(any(MovimentacaoVeiculo.class), eq(MovimentacaoVeiculoDTO.class)))
            .thenReturn(movimentacaoEntradaDTOMock);

        // Execução do método
        MovimentacaoVeiculoDTO resultado = movimentacaoVeiculoService
            .registrarMovimentacao(1L, 2L, TipoMovimentacao.ENTRADA);

        // Verificações
        assertNotNull(resultado);
        assertEquals(TipoMovimentacao.ENTRADA, resultado.getTipoMovimentacao());
        verify(movimentacaoVeiculoRepository).save(any(MovimentacaoVeiculo.class));
        verify(empresaRepository).findById(1L);
        verify(veiculoRepository).findById(2L);
    }


    @Test
    void testRegistrarMovimentacaoSaidaComSucesso() {
        // Criação de mocks para empresa e veículo
        Empresa empresaMock = new Empresa();
        empresaMock.setId(1L);
        empresaMock.setNome("Empresa Teste");
        empresaMock.setVagasCarros(10);

        Veiculo veiculoMock = new Veiculo();
        veiculoMock.setId(2L);
        veiculoMock.setMarca("Toyota");
        veiculoMock.setTipo(TipoVeiculo.CARRO);

        // Simulação do comportamento do repositório para empresa e veículo
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresaMock));
        when(veiculoRepository.findById(2L)).thenReturn(Optional.of(veiculoMock));

        // Simulação de que há uma entrada sem saída correspondente para o veículo
        when(movimentacaoVeiculoRepository.countEntradaSemSaida(1L, 2L)).thenReturn(1L);

        // Criação de mock de movimentação de saída
        MovimentacaoVeiculo movimentacaoSaidaMock = new MovimentacaoVeiculo();
        movimentacaoSaidaMock.setEmpresa(empresaMock);
        movimentacaoSaidaMock.setVeiculo(veiculoMock);
        movimentacaoSaidaMock.setDataHora(LocalDateTime.now());
        movimentacaoSaidaMock.setTipoMovimentacao(TipoMovimentacao.SAIDA);

        // Simulação do comportamento do repositório de movimentação para salvar a saída
        when(movimentacaoVeiculoRepository.save(any(MovimentacaoVeiculo.class))).thenReturn(movimentacaoSaidaMock);

        // Simulação do comportamento do ModelMapper
        MovimentacaoVeiculoDTO movimentacaoSaidaDTOMock = new MovimentacaoVeiculoDTO();
        
        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setId(1L);
        empresaDTO.setNome("Empresa Teste");
        empresaDTO.setCnpj("12345678");
        empresaDTO.setEndereco("Rua Teste");
        empresaDTO.setTelefone("12345-6789");
        empresaDTO.setVagasMotos(5);
        empresaDTO.setVagasCarros(10);
        movimentacaoSaidaDTOMock.setEmpresa(empresaDTO);
        
        VeiculoDTO veiculoDTO = new VeiculoDTO();
        veiculoDTO.setMarca("Toyota");
        veiculoDTO.setModelo("Corolla");
        veiculoDTO.setCor("Preto");
        veiculoDTO.setPlaca("ABC-1234");
        veiculoDTO.setTipo(TipoVeiculo.CARRO); 
        movimentacaoSaidaDTOMock.setVeiculo(veiculoDTO);
        movimentacaoSaidaDTOMock.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentacaoSaidaDTOMock.setDataHora(LocalDateTime.now());

        when(modelMapper.map(any(MovimentacaoVeiculo.class), eq(MovimentacaoVeiculoDTO.class)))
            .thenReturn(movimentacaoSaidaDTOMock);

        // Execução do método
        MovimentacaoVeiculoDTO resultado = movimentacaoVeiculoService.registrarSaida(1L, 2L);

        // Verificações
        assertNotNull(resultado);
        assertEquals(TipoMovimentacao.SAIDA, resultado.getTipoMovimentacao());
        assertNotNull(resultado.getEmpresa());
        assertNotNull(resultado.getVeiculo());
        verify(movimentacaoVeiculoRepository).save(any(MovimentacaoVeiculo.class));
        verify(empresaRepository).findById(1L);
        verify(veiculoRepository).findById(2L);
        verify(movimentacaoVeiculoRepository).countEntradaSemSaida(1L, 2L);
    }




    @Test
    void testListarMovimentacoesComSucesso() {
        // Criação de mock da entidade MovimentacaoVeiculo
        MovimentacaoVeiculo movimentacaoMock = new MovimentacaoVeiculo();
        movimentacaoMock.setTipoMovimentacao(TipoMovimentacao.ENTRADA); // Defina o tipo de movimentação
        
        // Simulação do comportamento do repositório
        when(movimentacaoVeiculoRepository.findAll()).thenReturn(Arrays.asList(movimentacaoMock));

        // Simulação do comportamento do ModelMapper para mapear MovimentacaoVeiculo -> MovimentacaoVeiculoDTO
        MovimentacaoVeiculoDTO movimentacaoDTO = new MovimentacaoVeiculoDTO();
        movimentacaoDTO.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        when(modelMapper.map(any(MovimentacaoVeiculo.class), eq(MovimentacaoVeiculoDTO.class))).thenReturn(movimentacaoDTO);

        // Execução do método
        List<MovimentacaoVeiculoDTO> resultado = movimentacaoVeiculoService.listarMovimentacoes();

        // Verificações
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertNotNull(resultado.get(0).getTipoMovimentacao());
        assertEquals(TipoMovimentacao.ENTRADA, resultado.get(0).getTipoMovimentacao());
    }
}
