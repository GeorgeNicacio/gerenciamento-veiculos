package br.com.fcamara.controleveiculos.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioEntradaSaidaPorHoraDTO {
    private LocalDateTime hora;
    private long entradas;
    private long saidas;
}