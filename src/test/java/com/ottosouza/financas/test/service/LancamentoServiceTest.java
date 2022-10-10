package com.ottosouza.financas.test.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ottosouza.financas.model.entity.Lancamento;
import com.ottosouza.financas.model.entity.StatusLancamento;
import com.ottosouza.financas.repositories.LancamentoRepository;
import com.ottosouza.financas.services.impl.LancamentoServiceImpl;
import com.ottosouza.financas.test.repository.LancamentoRepositoryTest;


@ExtendWith(SpringExtension.class)
public class LancamentoServiceTest {

	// testar os metodos reais
	@SpyBean
	private LancamentoServiceImpl service;

	// simular comportamento
	@MockBean
	private LancamentoRepository repo;
	
	@Test
	public void deveSalvarUmLancamento() {
		//cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.EFETIVADO);
		Mockito.when(repo.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		//execucao
		service.salvar(lancamentoASalvar);
		
		Assertions.assertThat(lancamentoSalvo.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamentoSalvo.getStatus()).isEqualTo(StatusLancamento.EFETIVADO);
	}
	
//	@Test
//	public void naoDeveSalvarUmLancamentoQuandoHouverErrorDeValidacao() {
//		
//	}
}
