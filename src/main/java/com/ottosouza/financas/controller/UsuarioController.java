package com.ottosouza.financas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ottosouza.financas.dto.UsuarioDTO;
import com.ottosouza.financas.exception.ErrorAutenticacao;
import com.ottosouza.financas.exception.RegraNegocioException;
import com.ottosouza.financas.model.entity.Usuario;
import com.ottosouza.financas.services.UsuarioService;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
	@Autowired
	private UsuarioService service;
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		Usuario usuario = Usuario.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {		
		try {
			Usuario autenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return new ResponseEntity(autenticado, HttpStatus.OK);
		} catch (ErrorAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	
	}
	

}
