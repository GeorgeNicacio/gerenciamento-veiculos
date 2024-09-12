package br.com.fcamara.controleveiculos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fcamara.controleveiculos.model.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
	Optional<Empresa> findByCnpj(String cnpj);
}