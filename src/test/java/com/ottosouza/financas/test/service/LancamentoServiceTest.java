package com.ottosouza.financas.test.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ottosouza.financas.exception.RegraNegocioException;
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
		// cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();

		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.EFETIVADO);
		Mockito.when(repo.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

		// execucao
		service.salvar(lancamentoASalvar);

		// verificacao
		Assertions.assertThat(lancamentoSalvo.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamentoSalvo.getStatus()).isEqualTo(StatusLancamento.EFETIVADO);
	}

	@Test
	public void deveAtualizarUmLancamento() {
		// cenario
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.EFETIVADO);

		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		Mockito.when(repo.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

		// execucao
		service.salvar(lancamentoSalvo);

		// verificacao
		Mockito.verify(repo, Mockito.times(1)).save(lancamentoSalvo);
	}

	@Test
	public void naoDeveAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		// cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();


		// execucao e verificacao
		Assertions.catchThrowableOfType(() -> service.atualizar(lancamentoASalvar), NullPointerException.class);
		Mockito.verify(repo, Mockito.never()).save(lancamentoASalvar);
	}

	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErrorDeValidacao() {
		// cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
		;

		// execucao e verificacao

		Assertions.catchThrowableOfType(() -> service.salvar(lancamentoASalvar), RegraNegocioException.class);
		Mockito.verify(repo, Mockito.never()).save(lancamentoASalvar);
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		//execucao
		service.deletar(lancamento);
		
		// verificacao
		Mockito.verify(repo).delete(lancamento);
	}
	@Test
	public void naoDeveDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		//execucao
		Assertions.catchThrowableOfType(() -> service.deletar(lancamento), RegraNegocioException.class);
		
		// verificacao
		Mockito.verify(repo, Mockito.never()).delete(lancamento);;
	}
	
	@Test	
	public void deveFiltrarUmLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> lista = java.util.Arrays.asList(lancamento);
		
		Mockito.when(repo.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		// execucao
		List<Lancamento> resultado = service.buscar(lancamento);
	
		Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
	}
	
	@Test
	public void deveAtualizarOStatusDeUmLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		StatusLancamento novoStatus  = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
		
		
		service.atualizarStatus(lancamento, novoStatus);
		
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);
		
	}
	
	@Test
	public void deveObterPorId() {
		Long id = 1l;
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repo.findById(id)).thenReturn(Optional.of(lancamento));
		
		Optional<Lancamento> resultado = service.buscarPorId(id);
		
		Assertions.assertThat(resultado.isPresent()).isTrue();
	}
	
	@Test
	public void naoDeveObterPorId() {
		Long id = 1l;
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repo.findById(id)).thenReturn(Optional.empty());
		
		Optional<Lancamento> resultado = service.buscarPorId(id);
		
		Assertions.assertThat(resultado.isPresent()).isFalse();
	}
}
