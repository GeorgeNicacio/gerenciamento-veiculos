package br.com.fcamara.controleveiculos.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.fcamara.controleveiculos.model.MovimentacaoVeiculo;
import br.com.fcamara.controleveiculos.model.Veiculo;
import br.com.fcamara.controleveiculos.model.enums.TipoVeiculo;
import br.com.fcamara.controleveiculos.repository.MovimentacaoVeiculoRepository;
import br.com.fcamara.controleveiculos.repository.VeiculoRepository;

@SpringBootTest
@AutoConfigureMockMvc
class MovimentacaoVeiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovimentacaoVeiculoRepository movimentacaoVeiculoRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    private Veiculo veiculo;

    @BeforeEach
    void setUp() {
        movimentacaoVeiculoRepository.deleteAll();
        veiculoRepository.deleteAll();

        veiculo = new Veiculo();
        veiculo.setMarca("Toyota");
        veiculo.setModelo("Corolla");
        veiculo.setCor("Preto");
        veiculo.setPlaca("ABC-1234");
        veiculo.setTipo(TipoVeiculo.CARRO);

        veiculo = veiculoRepository.save(veiculo);
    }

    @Test
    void testRegistrarEntradaVeiculo() throws Exception {
        mockMvc.perform(post("/api/movimentacoes/entrada")
                .param("veiculoId", String.valueOf(veiculo.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.veiculo.id", is(veiculo.getId().intValue())))
                .andExpect(jsonPath("$.entrada").exists())
                .andExpect(jsonPath("$.saida").doesNotExist());
    }

    @Test
    void testRegistrarSaidaVeiculo() throws Exception {
        MovimentacaoVeiculo movimentacaoVeiculo = new MovimentacaoVeiculo();
        movimentacaoVeiculo.setVeiculo(veiculo);
        movimentacaoVeiculo.setDataHora(LocalDateTime.now());
        movimentacaoVeiculo = movimentacaoVeiculoRepository.save(movimentacaoVeiculo);

        mockMvc.perform(post("/api/movimentacoes/saida")
                .param("movimentacaoId", String.valueOf(movimentacaoVeiculo.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.veiculo.id", is(veiculo.getId().intValue())))
                .andExpect(jsonPath("$.entrada").exists())
                .andExpect(jsonPath("$.saida").exists());
    }

    @Test
    void testBuscarMovimentacaoPorId() throws Exception {
        MovimentacaoVeiculo movimentacaoVeiculo = new MovimentacaoVeiculo();
        movimentacaoVeiculo.setVeiculo(veiculo);
        movimentacaoVeiculo.setDataHora(LocalDateTime.now());
        movimentacaoVeiculo = movimentacaoVeiculoRepository.save(movimentacaoVeiculo);

        mockMvc.perform(get("/api/movimentacoes/" + movimentacaoVeiculo.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.veiculo.id", is(veiculo.getId().intValue())))
                .andExpect(jsonPath("$.entrada").exists());
    }
}
