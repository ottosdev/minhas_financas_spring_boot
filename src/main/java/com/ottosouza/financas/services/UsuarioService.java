package com.ottosouza.financas.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ottosouza.financas.model.entity.Usuario;


public interface UsuarioService {
	Usuario autenticar(String email, String senha);
	Usuario salvarUsuario(Usuario usuario);
	void validarEmail(String email);
	Optional<Usuario>  obterUsuarioId(Long id);
}
