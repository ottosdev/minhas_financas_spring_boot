package com.ottosouza.financas.test.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ottosouza.financas.model.entity.Lancamento;
import com.ottosouza.financas.model.entity.StatusLancamento;
import com.ottosouza.financas.model.entity.TipoLancamento;
import com.ottosouza.financas.repositories.LancamentoRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {

	@Autowired
	LancamentoRepository repo;

	@Autowired
	TestEntityManager managerTest;

	@Test
	public void deveSalvarLancamento() {
		Lancamento lancamento = criarLancamento();
		lancamento = repo.save(lancamento);
		Assertions.assertThat(lancamento.getId()).isNotNull();
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = criarEPersistirUmLancamento();
		lancamento = managerTest.find(Lancamento.class, lancamento.getId());
		repo.delete(lancamento);
		Lancamento lancamentoInexistente = managerTest.find(Lancamento.class, lancamento.getId());
		Assertions.assertThat(lancamentoInexistente).isNull();
	}
	
	@Test
	public void deveAtualizarUmLancamento()
	{
		Lancamento lancamento = criarEPersistirUmLancamento();
		lancamento.setAno(2018);
		lancamento.setMes(2);
		lancamento.setDescricao("OEEE");
		repo.save(lancamento);
		Lancamento lancamentoAtualizado = managerTest.find(Lancamento.class, lancamento.getId());
		Assertions.assertThat(lancamentoAtualizado.getAno()).isEqualTo(2018);
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lancamento lancamento = criarEPersistirUmLancamento();	
		Optional<Lancamento> lancamentoEncontrado = repo.findById(lancamento.getId());
		Assertions.assertThat(lancamentoEncontrado.isPresent()).isTrue();
	}
	
	private Lancamento criarEPersistirUmLancamento() {
		Lancamento lancamento = criarLancamento();
		managerTest.persist(lancamento);
		return lancamento;
	}
	
	

	static public Lancamento criarLancamento() {
		Lancamento lancamento = Lancamento.builder().ano(2020).mes(1).descricao("lancamento x").valor(BigDecimal.valueOf(10))
				.tipo(TipoLancamento.RECEITA).status(StatusLancamento.PENDENTE).build();
		return lancamento;
	}
}
