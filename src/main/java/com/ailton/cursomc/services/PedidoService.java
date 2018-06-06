package com.ailton.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ailton.cursomc.domain.ItemPedido;
import com.ailton.cursomc.domain.PagamentoComBoleto;
import com.ailton.cursomc.domain.Pedido;
import com.ailton.cursomc.domain.enums.EstadoPagamento;
import com.ailton.cursomc.repositories.ItemPedidoRepository;
import com.ailton.cursomc.repositories.PagamentoRepository;
import com.ailton.cursomc.repositories.PedidoRepository;
import com.ailton.cursomc.repositories.ProdutoRepository;
import com.ailton.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	public Pedido find(Integer id) {
		/*
		 * Trocando método findOne(Java 7) padrão em repo pelo findById (Java 8) com uso da classe Optional
		 * que permite retornar a instância ou um valor nulo caso o item não seja encontrado.
		 * */
		Optional<Pedido> obj = repo.findById(id);
		//return obj.orElse(null);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()
			)
		);  		
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto  = (PagamentoComBoleto) obj.getPagamento();
			//setando data de pagamento com +7
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			Double preco = produtoService.find(ip.getProduto().getId()).getPreco();
			ip.setPreco(preco);
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		return obj;
	}
}
