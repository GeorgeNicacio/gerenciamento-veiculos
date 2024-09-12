package br.com.fcamara.controleveiculos.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import br.com.fcamara.controleveiculos.dtos.VeiculoDTO;
import br.com.fcamara.controleveiculos.model.Veiculo;

public interface VeiculoService {
	VeiculoDTO salvarVeiculo(Veiculo veiculo);
    List<VeiculoDTO> listarVeiculos();
    Optional<Veiculo> buscarVeiculoPorId(Long id);
    VeiculoDTO atualizarVeiculo(Long id, Veiculo veiculo);
    void deletarVeiculo(Long id);
    VeiculoDTO cadastrarVeiculo(Veiculo veiculo, Set<Long> empresaIds);
}
