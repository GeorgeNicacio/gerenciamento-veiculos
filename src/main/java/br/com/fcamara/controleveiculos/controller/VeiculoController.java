package br.com.fcamara.controleveiculos.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fcamara.controleveiculos.dtos.VeiculoDTO;
import br.com.fcamara.controleveiculos.model.Veiculo;
import br.com.fcamara.controleveiculos.service.VeiculoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;
    
 // Endpoint para cadastrar veículo, com ou sem empresas associadas
    @PostMapping(value = "/cadastrar", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<VeiculoDTO> cadastrarVeiculo(@Valid @RequestBody VeiculoDTO veiculoDTO) {
        Veiculo veiculo = new Veiculo();
        veiculo.setMarca(veiculoDTO.getMarca());
        veiculo.setModelo(veiculoDTO.getModelo());
        veiculo.setCor(veiculoDTO.getCor());
        veiculo.setPlaca(veiculoDTO.getPlaca());
        veiculo.setTipo(veiculoDTO.getTipo());

        Set<Long> empresaIds = veiculoDTO.getEmpresaIds() != null 
                                ? veiculoDTO.getEmpresaIds().stream().collect(Collectors.toSet()) 
                                : null;

        VeiculoDTO veiculoSalvo = veiculoService.cadastrarVeiculo(veiculo, empresaIds);
        return ResponseEntity.ok(veiculoSalvo);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<VeiculoDTO> listarVeiculos() {
        return veiculoService.listarVeiculos();
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Veiculo> buscarVeiculoPorId(@PathVariable Long id) {
        return veiculoService.buscarVeiculoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<VeiculoDTO> atualizarVeiculo(@PathVariable Long id, @Validated @RequestBody Veiculo veiculo) {
        try {
        	VeiculoDTO veiculoAtualizado = veiculoService.atualizarVeiculo(id, veiculo);
            return ResponseEntity.ok(veiculoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Void> deletarVeiculo(@PathVariable Long id) {
        try {
            veiculoService.deletarVeiculo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
