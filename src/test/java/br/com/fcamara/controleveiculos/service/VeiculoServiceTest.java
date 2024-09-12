package br.com.fcamara.controleveiculos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fcamara.controleveiculos.dtos.VeiculoDTO;
import br.com.fcamara.controleveiculos.model.Veiculo;
import br.com.fcamara.controleveiculos.model.enums.TipoVeiculo;
import br.com.fcamara.controleveiculos.repository.VeiculoRepository;
import br.com.fcamara.controleveiculos.service.impl.VeiculoServiceImpl;

class VeiculoServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private VeiculoServiceImpl veiculoService;

    private Veiculo veiculo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setMarca("Ford");
        veiculo.setModelo("Fiesta");
        veiculo.setCor("Azul");
        veiculo.setPlaca("ABC1234");
        veiculo.setTipo(TipoVeiculo.CARRO);
    }

    @Test
    void testSalvarVeiculo() {
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);
        
        VeiculoDTO savedVeiculo = veiculoService.salvarVeiculo(veiculo);
        
        assertNotNull(savedVeiculo);
        assertEquals("Ford", savedVeiculo.getMarca());
        verify(veiculoRepository, times(1)).save(veiculo);
    }

    @Test
    void testBuscarVeiculoPorId() {
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        
        Optional<Veiculo> foundVeiculo = veiculoService.buscarVeiculoPorId(1L);
        
        assertTrue(foundVeiculo.isPresent());
        assertEquals("Ford", foundVeiculo.get().getMarca());
        verify(veiculoRepository, times(1)).findById(1L);
    }

    @Test
    void testAtualizarVeiculo() {
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        VeiculoDTO updatedVeiculo = veiculoService.atualizarVeiculo(1L, veiculo);
        
        assertNotNull(updatedVeiculo);
        assertEquals("Ford", updatedVeiculo.getMarca());
        verify(veiculoRepository, times(1)).findById(1L);
        verify(veiculoRepository, times(1)).save(veiculo);
    }

    @Test
    void testDeletarVeiculo() {
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        doNothing().when(veiculoRepository).delete(veiculo);
        
        veiculoService.deletarVeiculo(1L);
        
        verify(veiculoRepository, times(1)).findById(1L);
        verify(veiculoRepository, times(1)).delete(veiculo);
    }
}
