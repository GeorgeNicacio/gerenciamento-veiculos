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

import br.com.fcamara.controleveiculos.config.jwt.model.User;
import br.com.fcamara.controleveiculos.dtos.EmpresaDTO;
import br.com.fcamara.controleveiculos.model.Empresa;
import br.com.fcamara.controleveiculos.repository.EmpresaRepository;
import br.com.fcamara.controleveiculos.service.impl.EmpresaServiceImpl;

class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private EmpresaServiceImpl empresaService;

    private Empresa empresa;
    
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNome("Empresa Exemplo");
        empresa.setCnpj("12345678000199");
        empresa.setEndereco("Rua Exemplo, 123");
        empresa.setTelefone("1234567890");
        empresa.setVagasMotos(10);
        empresa.setVagasCarros(20);
        
        user = new User();
        user.setId(1L);
        user.setUsername("12345678000199");
        user.setPassword("123456");
        user.setEmpresa(empresa);
    }

    @Test
    void testSalvarEmpresa() {
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

        EmpresaDTO novaEmpresa = empresaService.salvarEmpresa(empresa, user);

        assertNotNull(novaEmpresa);
        assertEquals("Empresa Exemplo", novaEmpresa.getNome());
        verify(empresaRepository, times(1)).save(empresa);
    }

    @Test
    void testBuscarEmpresaPorId() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));

        Optional<Empresa> foundEmpresa = empresaService.buscarEmpresaPorId(1L);

        assertTrue(foundEmpresa.isPresent());
        assertEquals("Empresa Exemplo", foundEmpresa.get().getNome());
        verify(empresaRepository, times(1)).findById(1L);
    }

    @Test
    void testAtualizarEmpresa() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

        EmpresaDTO empresaAtualizada = empresaService.atualizarEmpresa(1L, empresa);

        assertNotNull(empresaAtualizada);
        assertEquals("Empresa Exemplo", empresaAtualizada.getNome());
        verify(empresaRepository, times(1)).findById(1L);
        verify(empresaRepository, times(1)).save(empresa);
    }

    @Test
    void testDeletarEmpresa() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        doNothing().when(empresaRepository).delete(empresa);

        empresaService.deletarEmpresa(1L);

        verify(empresaRepository, times(1)).findById(1L);
        verify(empresaRepository, times(1)).delete(empresa);
    }
}