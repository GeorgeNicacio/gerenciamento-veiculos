package br.com.fcamara.controleveiculos.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fcamara.controleveiculos.config.jwt.model.User;
import br.com.fcamara.controleveiculos.config.jwt.repository.UserRepository;
import br.com.fcamara.controleveiculos.dtos.EmpresaDTO;
import br.com.fcamara.controleveiculos.model.Empresa;
import br.com.fcamara.controleveiculos.model.Veiculo;
import br.com.fcamara.controleveiculos.repository.EmpresaRepository;
import br.com.fcamara.controleveiculos.repository.VeiculoRepository;
import br.com.fcamara.controleveiculos.service.EmpresaService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmpresaServiceImpl implements EmpresaService {

	 	@Autowired
	    private EmpresaRepository empresaRepository;
	 	
	 	 @Autowired
	     private VeiculoRepository veiculoRepository;
	 	 
	 	 @Autowired
	     private UserRepository userRepository;

	     @Autowired
	     private PasswordEncoder passwordEncoder;
	     
	     @Autowired
	     private ModelMapper modelMapper;

	    @Override
	    public EmpresaDTO salvarEmpresa(Empresa empresa, User user) {
	    	 try {
		    	user.setEmpresa(empresa);
		    	 // Criptografando a senha do usuário
		        user.setPassword(passwordEncoder.encode(user.getPassword()));
		        userRepository.save(user);
	
		        // Associando o usuário à empresa
		        empresa.setUser(user);
	        
	            return convertToDto(empresaRepository.save(empresa));
	        } catch (DataIntegrityViolationException e) {
	            // Captura a exceção de violação de unicidade no banco de dados
	            throw new RuntimeException("CNPJ já cadastrado no sistema.");
	        }

	    }

	    @Override
	    public List<EmpresaDTO> listarEmpresas() {
	    	 List<Empresa> empresas = empresaRepository.findAll(); 
	         // Converte a lista de empresas para EmpresasDTO
	         return empresas.stream()
	                        .map(this::convertToDto)
	                        .collect(Collectors.toList());
	    }

	    @Override
	    public Optional<Empresa> buscarEmpresaPorId(Long id) {
	        return empresaRepository.findById(id);
	    }

	    @Override
	    public EmpresaDTO atualizarEmpresa(Long id, Empresa empresa) {
	        return empresaRepository.findById(id).map(empresaExistente -> {
	            empresaExistente.setNome(empresa.getNome());
	            empresaExistente.setCnpj(empresa.getCnpj());
	            empresaExistente.setEndereco(empresa.getEndereco());
	            empresaExistente.setTelefone(empresa.getTelefone());
	            empresaExistente.setVagasMotos(empresa.getVagasMotos());
	            empresaExistente.setVagasCarros(empresa.getVagasCarros());
	            return convertToDto(empresaRepository.save(empresaExistente));
	        }).orElseThrow(() -> new RuntimeException("Empresa não encontrada com id " + id));
	    }

	    @Override
	    public void deletarEmpresa(Long id) {
	        empresaRepository.deleteById(id);
	    }
	    
	 // Método para associar um veículo a uma empresa
	    public EmpresaDTO associarVeiculo(Long empresaId, Long veiculoId) {
	        Optional<Empresa> empresaOpt = empresaRepository.findById(empresaId);
	        Optional<Veiculo> veiculoOpt = veiculoRepository.findById(veiculoId);

	        if (empresaOpt.isPresent() && veiculoOpt.isPresent()) {
	            Empresa empresa = empresaOpt.get();
	            Veiculo veiculo = veiculoOpt.get();
	            empresa.addVeiculo(veiculo);  // Usa método auxiliar para manter consistência
	            return convertToDto(empresaRepository.save(empresa));
	        } else {
	            throw new RuntimeException("Empresa ou veículo não encontrado");
	        }
	    }
	    
	 // Método de conversão
	    private EmpresaDTO convertToDto(Empresa empresa) {
	        return modelMapper.map(empresa, EmpresaDTO.class); // Mapeamento de Veiculo para VeiculoDTO
	    }
}