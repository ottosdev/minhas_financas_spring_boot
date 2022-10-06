package com.ottosouza.financas.test.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ottosouza.financas.exception.ErrorAutenticacao;
import com.ottosouza.financas.exception.RegraNegocioException;
import com.ottosouza.financas.model.entity.Usuario;
import com.ottosouza.financas.repositories.UsuarioRepository;
import com.ottosouza.financas.services.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
public class UsuarioServiceTest {

	@SpyBean
	private UsuarioServiceImpl service;

	@MockBean
	private UsuarioRepository repo;

//	@BeforeEach
//	public void setup() {
//		service = Mockito.spy(UsuarioServiceImpl.class);
//	}

	@Test
	public void deveValidarEmail() {
		// cenario
		Mockito.when(repo.existsByEmail(Mockito.anyString())).thenReturn(false);
		// action
		service.validarEmail("ottos@gmail.com");
	}

	@Test
	public void deveLancarErrorQuandoExistirEmail() {
		assertThrows(RegraNegocioException.class, () -> {
			Mockito.when(repo.existsByEmail(Mockito.anyString())).thenReturn(true);
			service.validarEmail("ottos@gmail.com");
		});

	}

//	
	@Test
	public void deveAutenticarComSucesso() {

		String email = "ottos@email.com";
		String senha = "senha";
		Usuario u = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repo.findByEmail(email)).thenReturn(Optional.of(u));
		Usuario rs = service.autenticar(email, senha);
		Assertions.assertThat(rs).isNotNull();

	}

//	
	@Test
	public void deveLancarErrorQuandoNaoEncontrarUsuarioCadastrado() {
		Throwable exception = assertThrows(ErrorAutenticacao.class, () -> {

			Mockito.when(repo.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			service.autenticar("ottos@emai.com", "senha");

		});
		Assertions.assertThat(exception).isInstanceOf(ErrorAutenticacao.class).hasMessage("Usuario nao encontrado");
	}

	@Test
	public void deveLancarErrorQUandoSenhaNaoBater() {
		String email = "ottos@email.com";
		Throwable exception = assertThrows(ErrorAutenticacao.class, () -> {
			String senha = "senha";
			Usuario u = Usuario.builder().email(email).senha(senha).id(1l).build();
			Mockito.when(repo.findByEmail(Mockito.anyString())).thenReturn(Optional.of(u));

			service.autenticar(email, "123");

		});

		Assertions.assertThat(exception).isInstanceOf(ErrorAutenticacao.class).hasMessage("Senha/Email invalidos");

	}
	
	@Test
	public void deveSalvarUmUsuario() {
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
	}
	
	@Test
	public void naodeveSalvarUmUsuarioComEmailJaCadastrado() {
		assertThrows(RegraNegocioException.class, () -> {
			String email = "ottos@email.com";
			Usuario u = Usuario.builder().email(email).build();
			Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
			
			service.salvarUsuario(u);
			
			Mockito.verify(repo, Mockito.never()).save(u);
			
		});
	
	}

}
