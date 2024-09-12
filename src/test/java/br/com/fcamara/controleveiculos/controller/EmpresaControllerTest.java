package br.com.fcamara.controleveiculos.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.fcamara.controleveiculos.model.Empresa;
import br.com.fcamara.controleveiculos.repository.EmpresaRepository;

@SpringBootTest
@AutoConfigureMockMvc
class EmpresaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpresaRepository empresaRepository;

    @BeforeEach
    void setUp() {
        empresaRepository.deleteAll();
    }

    @Test
    void testCadastrarEmpresa() throws Exception {
        String empresaJson = "{\"nome\":\"Empresa Exemplo\",\"cnpj\":\"12345678000199\",\"endereco\":\"Rua Exemplo, 123\",\"telefone\":\"1234567890\",\"vagasParaMotos\":10,\"vagasParaCarros\":20}";

        mockMvc.perform(post("/api/empresas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(empresaJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Empresa Exemplo")))
                .andExpect(jsonPath("$.cnpj", is("12345678000199")));
    }

    @Test
    void testBuscarEmpresaPorId() throws Exception {
        Empresa empresa = new Empresa();
        empresa.setNome("Empresa Exemplo");
        empresa.setCnpj("12345678000199");
        empresa.setEndereco("Rua Exemplo, 123");
        empresa.setTelefone("1234567890");
        empresa.setVagasMotos(10);
        empresa.setVagasCarros(20);

        empresa = empresaRepository.save(empresa);

        mockMvc.perform(get("/api/empresas/" + empresa.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Empresa Exemplo")))
                .andExpect(jsonPath("$.cnpj", is("12345678000199")));
    }

    @Test
    void testAtualizarEmpresa() throws Exception {
        Empresa empresa = new Empresa();
        empresa.setNome("Empresa Exemplo");
        empresa.setCnpj("12345678000199");
        empresa.setEndereco("Rua Exemplo, 123");
        empresa.setTelefone("1234567890");
        empresa.setVagasMotos(10);
        empresa.setVagasCarros(20);

        empresa = empresaRepository.save(empresa);

        String empresaAtualizadaJson = "{\"nome\":\"Empresa Atualizada\",\"cnpj\":\"12345678000199\",\"endereco\":\"Rua Nova, 456\",\"telefone\":\"0987654321\",\"vagasParaMotos\":15,\"vagasParaCarros\":25}";

        mockMvc.perform(put("/api/empresas/" + empresa.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(empresaAtualizadaJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Empresa Atualizada")))
                .andExpect(jsonPath("$.endereco", is("Rua Nova, 456")));
    }

    @Test
    void testDeletarEmpresa() throws Exception {
        Empresa empresa = new Empresa();
        empresa.setNome("Empresa Exemplo");
        empresa.setCnpj("12345678000199");
        empresa.setEndereco("Rua Exemplo, 123");
        empresa.setTelefone("1234567890");
        empresa.setVagasMotos(10);
        empresa.setVagasCarros(20);

        empresa = empresaRepository.save(empresa);

        mockMvc.perform(delete("/api/empresas/" + empresa.getId()))
                .andExpect(status().isNoContent());
    }
}