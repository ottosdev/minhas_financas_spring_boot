package com.ottosouza.financas.services.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ottosouza.financas.exception.ErrorAutenticacao;
import com.ottosouza.financas.exception.RegraNegocioException;
import com.ottosouza.financas.model.entity.Usuario;
import com.ottosouza.financas.repositories.UsuarioRepository;
import com.ottosouza.financas.services.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	
	private UsuarioRepository repo;
	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repo) {
		super();
		this.repo = repo;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repo.findByEmail(email);
		if(!usuario.isPresent()) {
			throw new ErrorAutenticacao("Usuario nao encontrado");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErrorAutenticacao("Senha/Email invalidos");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repo.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repo.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Ja existe um usuario Cadastrado com este email");
		}
	}
	
	

}
