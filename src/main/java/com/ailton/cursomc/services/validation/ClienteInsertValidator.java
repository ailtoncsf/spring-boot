package com.ailton.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.ailton.cursomc.domain.Cliente;
import com.ailton.cursomc.domain.enums.TipoCliente;
import com.ailton.cursomc.dto.ClienteNewDTO;
import com.ailton.cursomc.repositories.ClienteRepository;
import com.ailton.cursomc.resources.exception.FieldMessage;
import com.ailton.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {		
	}
	
	@Override
	public boolean isValid(ClienteNewDTO objDTo, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();

		if(objDTo.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDTo.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		
		if(objDTo.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ((objDTo.getCpfOuCnpj()))) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		
		Cliente aux = repo.findByEmail(objDTo.getEmail());
		
		if(aux != null) {
			list.add(new FieldMessage("Email", "E-mail já existente."));
		}
		
		//incluir testes aqui, inserindo erros na lista
		for(FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		
		return list.isEmpty();
	}
}
