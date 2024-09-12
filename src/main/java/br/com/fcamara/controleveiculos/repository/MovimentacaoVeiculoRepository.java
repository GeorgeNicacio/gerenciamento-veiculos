package br.com.fcamara.controleveiculos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.fcamara.controleveiculos.model.MovimentacaoVeiculo;
import br.com.fcamara.controleveiculos.model.enums.TipoMovimentacao;

public interface MovimentacaoVeiculoRepository extends JpaRepository<MovimentacaoVeiculo, Long> {
	
	// Contar entradas por empresa e período
    @Query("SELECT COUNT(m) FROM MovimentacaoVeiculo m WHERE m.empresa.id = :empresaId AND m.tipoMovimentacao = :tipo AND m.dataHora BETWEEN :dataInicio AND :dataFim")
    long countMovimentacoesByTipoAndPeriodo(Long empresaId, TipoMovimentacao tipo, LocalDateTime dataInicio, LocalDateTime dataFim);
    
    // Buscar movimentações por empresa entre duas datas
    @Query("SELECT m FROM MovimentacaoVeiculo m WHERE m.empresa.id = :empresaId AND m.dataHora BETWEEN :dataInicio AND :dataFim")
    List<MovimentacaoVeiculo> findAllByEmpresaAndPeriodo(Long empresaId, LocalDateTime dataInicio, LocalDateTime dataFim);
    
    // Buscar movimentações agrupadas por hora e tipo de movimentação
    @Query("SELECT HOUR(m.dataHora) AS hora, m.tipoMovimentacao, COUNT(m) FROM MovimentacaoVeiculo m WHERE m.empresa.id = :empresaId AND m.dataHora BETWEEN :dataInicio AND :dataFim GROUP BY HOUR(m.dataHora), m.tipoMovimentacao ORDER BY hora")
    List<Object[]> findMovimentacoesAgrupadasPorHora(Long empresaId, LocalDateTime dataInicio, LocalDateTime dataFim);
    
    // Contar o número de entradas de carros
    @Query("SELECT COUNT(m) FROM MovimentacaoVeiculo m WHERE m.empresa.id = :empresaId AND m.veiculo.tipo = 'Carro' AND m.tipoMovimentacao = 'ENTRADA'")
    long countEntradasCarros(Long empresaId);

    // Contar o número de saídas de carros
    @Query("SELECT COUNT(m) FROM MovimentacaoVeiculo m WHERE m.empresa.id = :empresaId AND m.veiculo.tipo = 'Carro' AND m.tipoMovimentacao = 'SAIDA'")
    long countSaidasCarros(Long empresaId);

    // Contar o número de entradas de motos
    @Query("SELECT COUNT(m) FROM MovimentacaoVeiculo m WHERE m.empresa.id = :empresaId AND m.veiculo.tipo = 'Moto' AND m.tipoMovimentacao = 'ENTRADA'")
    long countEntradasMotos(Long empresaId);

    // Contar o número de saídas de motos
    @Query("SELECT COUNT(m) FROM MovimentacaoVeiculo m WHERE m.empresa.id = :empresaId AND m.veiculo.tipo = 'Moto' AND m.tipoMovimentacao = 'SAIDA'")
    long countSaidasMotos(Long empresaId);
    
    // Verifica se o veículo está estacionado (tem uma entrada sem saída correspondente)
    @Query("SELECT COUNT(m) FROM MovimentacaoVeiculo m WHERE m.empresa.id = :empresaId AND m.veiculo.id = :veiculoId AND m.tipoMovimentacao = 'ENTRADA' AND NOT EXISTS (SELECT 1 FROM MovimentacaoVeiculo ms WHERE ms.empresa.id = :empresaId AND ms.veiculo.id = :veiculoId AND ms.tipoMovimentacao = 'SAIDA' AND ms.dataHora > m.dataHora)")
    long countVeiculoEstacionado(Long empresaId, Long veiculoId);

    // Verifica se existe uma entrada sem saída (para validar a saída)
    @Query("SELECT COUNT(m) FROM MovimentacaoVeiculo m WHERE m.empresa.id = :empresaId AND m.veiculo.id = :veiculoId AND m.tipoMovimentacao = 'ENTRADA' AND NOT EXISTS (SELECT 1 FROM MovimentacaoVeiculo ms WHERE ms.empresa.id = :empresaId AND ms.veiculo.id = :veiculoId AND ms.tipoMovimentacao = 'SAIDA' AND ms.dataHora > m.dataHora)")
    long countEntradaSemSaida(Long empresaId, Long veiculoId);
}
