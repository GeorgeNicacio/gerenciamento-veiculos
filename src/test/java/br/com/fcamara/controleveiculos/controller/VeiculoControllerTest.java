package br.com.fcamara.controleveiculos.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.fcamara.controleveiculos.model.Veiculo;
import br.com.fcamara.controleveiculos.model.enums.TipoVeiculo;
import br.com.fcamara.controleveiculos.repository.VeiculoRepository;

@SpringBootTest
@AutoConfigureMockMvc
class VeiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @BeforeEach
    void setUp() {
        veiculoRepository.deleteAll();
    }

    @Test
    void testCadastrarVeiculo() throws Exception {
        String veiculoJson = "{\"marca\":\"Ford\",\"modelo\":\"Fiesta\",\"cor\":\"Azul\",\"placa\":\"ABC1234\",\"tipo\":\"Carro\"}";

        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(veiculoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca", is("Ford")))
                .andExpect(jsonPath("$.modelo", is("Fiesta")));
    }

    @Test
    void testBuscarVeiculoPorId() throws Exception {
        Veiculo veiculo = new Veiculo();
        veiculo.setMarca("Ford");
        veiculo.setModelo("Fiesta");
        veiculo.setCor("Azul");
        veiculo.setPlaca("ABC1234");
        veiculo.setTipo(TipoVeiculo.CARRO);

        veiculo = veiculoRepository.save(veiculo);

        mockMvc.perform(get("/api/veiculos/" + veiculo.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca", is("Ford")))
                .andExpect(jsonPath("$.modelo", is("Fiesta")));
    }
}
