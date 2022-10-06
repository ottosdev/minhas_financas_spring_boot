package com.ottosouza.financas.test.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ottosouza.financas.model.entity.Usuario;
import com.ottosouza.financas.repositories.UsuarioRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repo;

	@Autowired
	TestEntityManager managerTest;
	
	

	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		// cenario
		Usuario u = criarUsuario();
		managerTest.persist(u);
		// acao
		boolean result = repo.existsByEmail("otto@gmail.com");
		// verificacao
		Assertions.assertThat(result).isTrue();
	}

	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		boolean result = repo.existsByEmail("otto@gmail.com");
		Assertions.assertThat(result).isFalse();
	}

	@Test
	public void devePersistirUmUsuarioNaBase() {
		// cenario
		Usuario u = criarUsuario();

		// acao
		Usuario usuarioSalvo = repo.save(u);
		
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUsuarioPorEmail() {
		Usuario u = criarUsuario();
		managerTest.persist(u);
		
	    Optional<Usuario> usuario = 	repo.findByEmail("otto_persiste@gmail.com");
	    Assertions.assertThat(usuario.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetonarVazioAoBuscarPorEmailQuandoNaoExistirNaBase() {
		Usuario u = criarUsuario();
		managerTest.persist(u);
		
	    Optional<Usuario> usuario = 	repo.findByEmail("otto_persiste@gmail.com");
	    Assertions.assertThat(usuario.isPresent()).isFalse();
	}
	
	
	public static Usuario criarUsuario() {
		return Usuario.builder().nome("usuario").email("otto_persiste@gmail.com").senha("1234").build();
	}

}
