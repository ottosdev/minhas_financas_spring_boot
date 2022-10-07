package com.ottosouza.financas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ottosouza.financas.dto.LancamentoDTO;
import com.ottosouza.financas.exception.RegraNegocioException;
import com.ottosouza.financas.model.entity.Lancamento;
import com.ottosouza.financas.model.entity.StatusLancamento;
import com.ottosouza.financas.model.entity.TipoLancamento;
import com.ottosouza.financas.model.entity.Usuario;
import com.ottosouza.financas.services.LancamentoService;
import com.ottosouza.financas.services.UsuarioService;

@RestController
@RequestMapping("/lancamento")
public class LancamentoController {

	
	@Autowired
	private LancamentoService service;
	
	@Autowired
	private UsuarioService serviceUsuario;
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento entidade = converter(dto);
			entidade = service.salvar(entidade);
			return ResponseEntity.ok(entidade);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
	
			return service.buscarPorId(id).map(item -> {
				try {
					Lancamento lancamento = converter(dto);
					lancamento.setId(item.getId());
					service.atualizar(lancamento);
					return ResponseEntity.ok(lancamento);
				} catch (RegraNegocioException e) {
					return ResponseEntity.badRequest().body(e.getMessage());
				}
			
			}).orElseGet( () ->  new ResponseEntity("Lancamento nao encontrado na base", HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable Long id) {
		return service.buscarPorId(id).map(item -> {
			try {
				service.deletar(item);
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		
		}).orElseGet( () ->  new ResponseEntity("Lancamento nao encontrado na base", HttpStatus.BAD_REQUEST));
	}
	@GetMapping
	public ResponseEntity buscar(
			@RequestParam(value = "descricao" , required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam(value = "idUsuario" ,required = true) Long idUsuario
			) {
		Lancamento filtro = new Lancamento();
		filtro.setDescricao(descricao);
		filtro.setAno(ano);
		filtro.setMes(mes);
		
		Optional<Usuario> u = serviceUsuario.obterUsuarioId(idUsuario);
		
		if(!u.isPresent()) {
			return ResponseEntity.badRequest().body("Usuario nao encontrado");
		}else {
			filtro.setUsuario(u.get());
			
		}
		
		List<Lancamento> lancamentos = service.buscar(filtro);
		return ResponseEntity.ok(lancamentos);
	}
	
	
	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		
		Usuario u = serviceUsuario.obterUsuarioId(dto.getUsuario()).orElseThrow(() -> new RegraNegocioException("Usuario nao existe"));
		lancamento.setUsuario(u);
		if(dto.getTipo() != null) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}
		
		if(dto.getStatus() != null ) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}

		return lancamento;
	}
	
}
