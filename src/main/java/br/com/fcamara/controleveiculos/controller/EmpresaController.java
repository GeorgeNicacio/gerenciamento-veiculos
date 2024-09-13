package br.com.fcamara.controleveiculos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fcamara.controleveiculos.dtos.EmpresaDTO;
import br.com.fcamara.controleveiculos.model.Empresa;
import br.com.fcamara.controleveiculos.service.EmpresaService;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

	@Autowired
    private EmpresaService empresaService;
	
	// Endpoint para associar um veículo a uma empresa
    @PostMapping(value = "/{empresaId}/veiculos/{veiculoId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<EmpresaDTO> associarVeiculo(@PathVariable Long empresaId, @PathVariable Long veiculoId) {
    	// Recupera a autenticação atual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cnpjAutenticado = authentication.getName();  // O nome será o CNPJ

        // Verifica se o CNPJ autenticado corresponde à empresa que está tentando cadastrar o veículo
        Empresa empresa = empresaService.buscarEmpresaPorId(empresaId).get();
        if (empresa != null && empresa.getCnpj().equals(cnpjAutenticado)) {
        	EmpresaDTO empresaAtualizada = empresaService.associarVeiculo(empresaId, veiculoId);
            return ResponseEntity.ok(empresaAtualizada);
        } else {
            return ResponseEntity.status(403).body(null);
        }
    }
	
    @PostMapping(value = "/cadastrar", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<EmpresaDTO> cadastrarEmpresa(@Validated @RequestBody Empresa empresa) {
    	EmpresaDTO novaEmpresa = empresaService.salvarEmpresa(empresa);
    	
        return ResponseEntity.ok(novaEmpresa);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<EmpresaDTO> listarEmpresas() {
        return empresaService.listarEmpresas();
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Empresa> buscarEmpresaPorId(@PathVariable Long id) {
        return empresaService.buscarEmpresaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<EmpresaDTO> atualizarEmpresa(@PathVariable Long id, @Validated @RequestBody Empresa empresa) {
        try {
            EmpresaDTO empresaAtualizada = empresaService.atualizarEmpresa(id, empresa);
            return ResponseEntity.ok(empresaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Void> deletarEmpresa(@PathVariable Long id) {
        try {
            empresaService.deletarEmpresa(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}