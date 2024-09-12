package br.com.fcamara.controleveiculos.service;


import java.util.List;
import java.util.Optional;

import br.com.fcamara.controleveiculos.config.jwt.model.User;
import br.com.fcamara.controleveiculos.dtos.EmpresaDTO;
import br.com.fcamara.controleveiculos.model.Empresa;

public interface EmpresaService {
    List<EmpresaDTO> listarEmpresas();
    Optional<Empresa> buscarEmpresaPorId(Long id);
    EmpresaDTO atualizarEmpresa(Long id, Empresa empresa);
    void deletarEmpresa(Long id);
    EmpresaDTO associarVeiculo(Long empresaId, Long veiculoId);
    EmpresaDTO salvarEmpresa(Empresa empresa, User user);
}