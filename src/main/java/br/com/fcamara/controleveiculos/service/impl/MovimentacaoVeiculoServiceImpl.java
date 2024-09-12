package br.com.fcamara.controleveiculos.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fcamara.controleveiculos.dtos.MovimentacaoVeiculoDTO;
import br.com.fcamara.controleveiculos.model.Empresa;
import br.com.fcamara.controleveiculos.model.MovimentacaoVeiculo;
import br.com.fcamara.controleveiculos.model.Veiculo;
import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;
import br.com.fcamara.controleveiculos.repository.EmpresaRepository;
import br.com.fcamara.controleveiculos.repository.MovimentacaoVeiculoRepository;
import br.com.fcamara.controleveiculos.repository.VeiculoRepository;
import br.com.fcamara.controleveiculos.service.MovimentacaoVeiculoService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MovimentacaoVeiculoServiceImpl implements MovimentacaoVeiculoService {

    @Autowired
    private MovimentacaoVeiculoRepository movimentacaoVeiculoRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;
    
    private final ModelMapper modelMapper;

    @Override
    public MovimentacaoVeiculoDTO registrarMovimentacao(Long empresaId, Long veiculoId, TipoMovimentacao tipoMovimentacao) {
    	if(tipoMovimentacao.equals(TipoMovimentacao.ENTRADA)) {
    		return registrarEntrada(empresaId, veiculoId);
    	} else {
    		return registrarSaida(empresaId, veiculoId);
    	}
    }

    @Override
    public List<MovimentacaoVeiculoDTO> listarMovimentacoes() {
        List<MovimentacaoVeiculo> list = movimentacaoVeiculoRepository.findAll(); 
        // Converte a lista de Veiculo para VeiculoDTO
        return list.stream()
                       .map(this::convertToDto)
                       .collect(Collectors.toList());
    }
    
    // Método para registrar a entrada de um veículo
    public MovimentacaoVeiculoDTO registrarEntrada(Long empresaId, Long veiculoId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
        
     // Verificar se o veículo já está estacionado (entrada sem saída)
        if (movimentacaoVeiculoRepository.countVeiculoEstacionado(empresaId, veiculoId) > 0) {
            throw new RuntimeException("O veículo já está estacionado e não pode registrar uma nova entrada até que saia.");
        }

        // Contar entradas e saídas de veículos
        long entradasCarros = movimentacaoVeiculoRepository.countEntradasCarros(empresaId);
        long saidasCarros = movimentacaoVeiculoRepository.countSaidasCarros(empresaId);
        long carrosEstacionados = entradasCarros - saidasCarros;

        long entradasMotos = movimentacaoVeiculoRepository.countEntradasMotos(empresaId);
        long saidasMotos = movimentacaoVeiculoRepository.countSaidasMotos(empresaId);
        long motosEstacionadas = entradasMotos - saidasMotos;

        // Verificar o tipo de veículo e controlar as vagas
        if ("CARRO".equalsIgnoreCase(veiculo.getTipo().name())) {
            if (carrosEstacionados >= empresa.getVagasCarros()) {
                throw new RuntimeException("Todas as vagas para carros estão ocupadas");
            }
        } else if ("MOTO".equalsIgnoreCase(veiculo.getTipo().name())) {
            if (motosEstacionadas >= empresa.getVagasMotos()) {
                throw new RuntimeException("Todas as vagas para motos estão ocupadas");
            }
        } else {
            throw new RuntimeException("Tipo de veículo inválido");
        }

        // Registrar a entrada do veículo
        MovimentacaoVeiculo movimentacao = new MovimentacaoVeiculo();
        movimentacao.setEmpresa(empresa);
        movimentacao.setVeiculo(veiculo);
        movimentacao.setDataHora(LocalDateTime.now());
        movimentacao.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        //movimentacaoVeiculoRepository.save(movimentacao);
        
        return convertToDto(movimentacaoVeiculoRepository.save(movimentacao));

    }
    
    // Método para registrar a saída de um veículo
    public MovimentacaoVeiculoDTO registrarSaida(Long empresaId, Long veiculoId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
        
     // Verificar se o veículo tem uma entrada sem saída correspondente
        if (movimentacaoVeiculoRepository.countEntradaSemSaida(empresaId, veiculoId) == 0) {
            throw new RuntimeException("O veículo não pode sair porque não há entrada registrada.");
        }

        // Registrar a saída do veículo
        MovimentacaoVeiculo movimentacao = new MovimentacaoVeiculo();
        movimentacao.setEmpresa(empresa);
        movimentacao.setVeiculo(veiculo);
        movimentacao.setDataHora(LocalDateTime.now());
        movimentacao.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        return convertToDto(movimentacaoVeiculoRepository.save(movimentacao));
    }
    
    // Método de conversão
    private MovimentacaoVeiculoDTO convertToDto(MovimentacaoVeiculo movimentacaoVeiculo) {
        return modelMapper.map(movimentacaoVeiculo, MovimentacaoVeiculoDTO.class); 
    }
}