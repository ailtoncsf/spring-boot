package com.ailton.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ailton.cursomc.domain.Categoria;
import com.ailton.cursomc.domain.Cliente;
import com.ailton.cursomc.repositories.ClienteRepository;
import com.ailton.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	public Cliente buscar(Integer id) {
		/*
		 * Trocando método findOne(Java 7) padrão em repo pelo findById (Java 8) com uso da classe Optional
		 * que permite retornar a instância ou um valor nulo caso o item não seja encontrado.
		 * */
		Optional<Cliente> obj = repo.findById(id);
		//return obj.orElse(null);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()
			)
		);  		
	}
}
