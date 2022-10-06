package com.ottosouza.financas.services;

import java.util.List;

import com.ottosouza.financas.model.entity.Lancamento;
import com.ottosouza.financas.model.entity.StatusLancamento;

public interface LancamentoService {

	
	Lancamento salvar(Lancamento lancamento);
	Lancamento atualizar(Lancamento lancamento);
	void deletar(Lancamento lancamento);
	List<Lancamento> buscar(Lancamento lancamanetoFiltro);
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);	
	void validar(Lancamento lancamento);
}
