package br.com.fcamara.controleveiculos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fcamara.controleveiculos.dtos.MovimentacaoVeiculoDTO;
import br.com.fcamara.controleveiculos.model.MovimentacaoVeiculo;
import br.com.fcamara.controleveiculos.model.Veiculo;
import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;
import br.com.fcamara.controleveiculos.model.enums.TipoVeiculo;
import br.com.fcamara.controleveiculos.repository.MovimentacaoVeiculoRepository;
import br.com.fcamara.controleveiculos.repository.VeiculoRepository;
import br.com.fcamara.controleveiculos.service.impl.MovimentacaoVeiculoServiceImpl;

class MovimentacaoVeiculoServiceTest {

    @Mock
    private MovimentacaoVeiculoRepository movimentacaoVeiculoRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private MovimentacaoVeiculoServiceImpl movimentacaoVeiculoService;

    private MovimentacaoVeiculo movimentacaoVeiculo;
    private Veiculo veiculo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setMarca("Toyota");
        veiculo.setModelo("Corolla");
        veiculo.setCor("Preto");
        veiculo.setPlaca("ABC-1234");
        veiculo.setTipo(TipoVeiculo.CARRO);

        movimentacaoVeiculo = new MovimentacaoVeiculo();
        movimentacaoVeiculo.setId(1L);
        movimentacaoVeiculo.setVeiculo(veiculo);
        movimentacaoVeiculo.setDataHora(LocalDateTime.now());
    }

    @Test
    void testRegistrarEntradaVeiculo() {
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(movimentacaoVeiculoRepository.save(any(MovimentacaoVeiculo.class))).thenReturn(movimentacaoVeiculo);

        MovimentacaoVeiculoDTO novaMovimentacao = movimentacaoVeiculoService.registrarMovimentacao(1L,1L, TipoMovimentacao.ENTRADA);

        assertNotNull(novaMovimentacao);
        assertEquals(veiculo, novaMovimentacao.getVeiculo());
        assertNotNull(novaMovimentacao.getDataHora());
        assertNull(novaMovimentacao.getDataHora());
        verify(movimentacaoVeiculoRepository, times(1)).save(any(MovimentacaoVeiculo.class));
    }

    @Test
    void testRegistrarSaidaVeiculo() {
        movimentacaoVeiculo.setDataHora(LocalDateTime.now());
        when(movimentacaoVeiculoRepository.findById(1L)).thenReturn(Optional.of(movimentacaoVeiculo));
        when(movimentacaoVeiculoRepository.save(any(MovimentacaoVeiculo.class))).thenReturn(movimentacaoVeiculo);

        MovimentacaoVeiculoDTO movimentacaoAtualizada = movimentacaoVeiculoService.registrarMovimentacao(1L, 1L, TipoMovimentacao.SAIDA);

        assertNotNull(movimentacaoAtualizada);
        assertEquals(veiculo, movimentacaoAtualizada.getVeiculo());
        assertNotNull(movimentacaoAtualizada.getDataHora());
        assertNotNull(movimentacaoAtualizada.getDataHora());
        verify(movimentacaoVeiculoRepository, times(1)).findById(1L);
        verify(movimentacaoVeiculoRepository, times(1)).save(any(MovimentacaoVeiculo.class));
    }

}