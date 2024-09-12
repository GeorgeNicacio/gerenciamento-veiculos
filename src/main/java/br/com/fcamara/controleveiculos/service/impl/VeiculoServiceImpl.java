package br.com.fcamara.controleveiculos.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fcamara.controleveiculos.dtos.VeiculoDTO;
import br.com.fcamara.controleveiculos.model.Empresa;
import br.com.fcamara.controleveiculos.model.Veiculo;
import br.com.fcamara.controleveiculos.repository.EmpresaRepository;
import br.com.fcamara.controleveiculos.repository.VeiculoRepository;
import br.com.fcamara.controleveiculos.service.VeiculoService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VeiculoServiceImpl implements VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;
    
    private final ModelMapper modelMapper;

    @Override
    public VeiculoDTO salvarVeiculo(Veiculo veiculo) {
        return convertToDto(veiculoRepository.save(veiculo));
    }

    @Override
    public List<VeiculoDTO> listarVeiculos() {
    	List<Veiculo> veiculos = veiculoRepository.findAll(); // Pega todos os veículos do banco
        // Converte a lista de Veiculo para VeiculoDTO
        return veiculos.stream()
                       .map(this::convertToDto)
                       .collect(Collectors.toList());
    }

    @Override
    public Optional<Veiculo> buscarVeiculoPorId(Long id) {
        return veiculoRepository.findById(id);
    }

    @Override
    public VeiculoDTO atualizarVeiculo(Long id, Veiculo veiculo) {
        return veiculoRepository.findById(id).map(veiculoExistente -> {
            veiculoExistente.setMarca(veiculo.getMarca());
            veiculoExistente.setModelo(veiculo.getModelo());
            veiculoExistente.setCor(veiculo.getCor());
            veiculoExistente.setPlaca(veiculo.getPlaca());
            veiculoExistente.setTipo(veiculo.getTipo());
            return convertToDto(veiculoRepository.save(veiculoExistente));
        }).orElseThrow(() -> new RuntimeException("Veículo não encontrado com id " + id));
    }

    @Override
    public void deletarVeiculo(Long id) {
        veiculoRepository.deleteById(id);
    }
    
    // Método para cadastrar um veículo, opcionalmente associado a uma ou mais empresas
    public VeiculoDTO cadastrarVeiculo(Veiculo veiculo, Set<Long> empresaIds) {
        if (empresaIds != null && !empresaIds.isEmpty()) {
            Set<Empresa> empresas = new HashSet<>();
            for (Long empresaId : empresaIds) {
                Optional<Empresa> empresaOpt = empresaRepository.findById(empresaId);
                if (empresaOpt.isPresent()) {
                    empresas.add(empresaOpt.get());
                } else {
                    throw new RuntimeException("Empresa com ID " + empresaId + " não encontrada");
                }
            }
            veiculo.setEmpresas(empresas);
            // Atualizar o lado inverso da relação
            /*for (Empresa empresa : empresas) {
                empresa.getVeiculos().add(veiculo);
            }*/
        }
        return convertToDto(veiculoRepository.save(veiculo));
    }
    
    // Método de conversão
    private VeiculoDTO convertToDto(Veiculo veiculo) {
        return modelMapper.map(veiculo, VeiculoDTO.class); // Mapeamento de Veiculo para VeiculoDTO
    }

}
