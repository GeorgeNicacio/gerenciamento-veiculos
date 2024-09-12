package br.com.fcamara.controleveiculos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import br.com.fcamara.controleveiculos.config.jwt.model.User;
import br.com.fcamara.controleveiculos.dtos.EmpresaDTO;
import br.com.fcamara.controleveiculos.model.Empresa;
import br.com.fcamara.controleveiculos.service.EmpresaService;

@Controller
public class EmpresaController {

	@Autowired
    private EmpresaService empresaService;
	
	// Endpoint para associar um veículo a uma empresa
	@MutationMapping
    public EmpresaDTO associarVeiculo(@Argument Long empresaId, @Argument Long veiculoId) {
    	// Recupera a autenticação atual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cnpjAutenticado = authentication.getName();  // O nome será o CNPJ

        // Verifica se o CNPJ autenticado corresponde à empresa que está tentando cadastrar o veículo
        Empresa empresa = empresaService.buscarEmpresaPorId(empresaId).get();
        if (empresa != null && empresa.getCnpj().equals(cnpjAutenticado)) {
        	EmpresaDTO empresaAtualizada = empresaService.associarVeiculo(empresaId, veiculoId);
            return empresaAtualizada;
        } else {
            return null;
        }
    }
	
	@MutationMapping
	public EmpresaDTO cadastrarEmpresa(@Argument Empresa input) {
	    // Criar uma nova instância de Empresa
	    Empresa empresa = new Empresa();
	    empresa.setNome(input.getNome());
	    empresa.setCnpj(input.getCnpj());
	    empresa.setEndereco(input.getEndereco());
	    empresa.setTelefone(input.getTelefone());
	    empresa.setVagasMotos(input.getVagasMotos());
	    empresa.setVagasCarros(input.getVagasCarros());

	    // Criar um novo usuário baseado no input
	    User user = new User();
	    user.setUsername(input.getUser().getUsername());
	    user.setPassword(input.getUser().getPassword());  // O método setPassword já cuida da criptografia

	    // Associar o usuário à empresa
	    empresa.setUser(user);

	    // Salvar a empresa e o usuário associados
	    EmpresaDTO novaEmpresa = empresaService.salvarEmpresa(empresa, user);

	    return novaEmpresa;
	}

    @QueryMapping
    public List<EmpresaDTO> listarEmpresas() {
        return empresaService.listarEmpresas();
    }

    @QueryMapping
    public ResponseEntity<Empresa> buscarEmpresaPorId(@Argument Long id) {
        return empresaService.buscarEmpresaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @MutationMapping
    public ResponseEntity<EmpresaDTO> atualizarEmpresa(@Argument Long id, @Argument Empresa input) {
        try {
            EmpresaDTO empresaAtualizada = empresaService.atualizarEmpresa(id, input);
            return ResponseEntity.ok(empresaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @MutationMapping
    public boolean deletarEmpresa(@Argument Long id) {
    	try {
            empresaService.deletarEmpresa(id);
            return true; // Retorna 'true' para indicar que a empresa foi deletada
        } catch (RuntimeException e) {
            return false; // Retorna 'false' se ocorrer algum erro
        }
    }
}