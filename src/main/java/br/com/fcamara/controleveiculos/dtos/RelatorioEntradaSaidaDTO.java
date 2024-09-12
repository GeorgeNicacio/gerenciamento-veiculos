package br.com.fcamara.controleveiculos.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioEntradaSaidaDTO {
    private long totalEntradas;
    private long totalSaidas;

}
