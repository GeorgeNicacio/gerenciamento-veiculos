package br.com.fcamara.controleveiculos.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import br.com.fcamara.controleveiculos.dtos.VeiculoDTO;
import br.com.fcamara.controleveiculos.model.Veiculo;
import br.com.fcamara.controleveiculos.service.VeiculoService;

@Controller
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;
    
 // Endpoint para cadastrar veículo, com ou sem empresas associadas
    @MutationMapping
    public VeiculoDTO cadastrarVeiculo(@Argument VeiculoDTO input) {
        if (input == null) {
            throw new IllegalArgumentException("O objeto input não pode ser nulo");
        }

        Veiculo veiculo = new Veiculo();
        veiculo.setMarca(input.getMarca());
        veiculo.setModelo(input.getModelo());
        veiculo.setCor(input.getCor());
        veiculo.setPlaca(input.getPlaca());
        veiculo.setTipo(input.getTipo());

        Set<Long> empresaIds = input.getEmpresaIds() != null 
                                    ? input.getEmpresaIds().stream().collect(Collectors.toSet()) 
                                    : null;

        VeiculoDTO veiculoSalvo = veiculoService.cadastrarVeiculo(veiculo, empresaIds);
        return veiculoSalvo;
    }

    @QueryMapping
    public List<VeiculoDTO> listarVeiculos() {
        return veiculoService.listarVeiculos();
    }

    @QueryMapping
    public Veiculo buscarVeiculoPorId(@Argument Long id) {
        return veiculoService.buscarVeiculoPorId(id).get();
    }

    @MutationMapping
    public VeiculoDTO atualizarVeiculo(@Argument Long id, @Argument Veiculo input) {
        try {
        	VeiculoDTO veiculoAtualizado = veiculoService.atualizarVeiculo(id, input);
            return veiculoAtualizado;
        } catch (RuntimeException e) {
            return null;
        }
    }

    @MutationMapping
    public boolean deletarVeiculo(@Argument Long id) {
        try {
            veiculoService.deletarVeiculo(id);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
