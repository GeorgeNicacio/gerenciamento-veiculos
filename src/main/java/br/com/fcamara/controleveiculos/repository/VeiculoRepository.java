package br.com.fcamara.controleveiculos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fcamara.controleveiculos.model.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
}
