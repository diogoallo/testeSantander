package br.com.testesantander.services;

import br.com.testesantander.converter.DozerConverter;
import br.com.testesantander.dtos.ClienteDto;
import br.com.testesantander.model.Cliente;
import br.com.testesantander.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServices {
	
	@Autowired
	ClienteRepository repository;

	@Autowired
	PagedResourcesAssembler<ClienteDto> assembler;

	private static final Logger log = LoggerFactory.getLogger(ClienteServices.class);

	public ClienteDto create(ClienteDto cliente) {
		var entity = DozerConverter.parseObject(cliente, Cliente.class);
		var vo = DozerConverter.parseObject(repository.save(entity), ClienteDto.class);
		return vo;
	}

	public List<ClienteDto> findAll() {
		return DozerConverter.parseListObjects(repository.findAll(), ClienteDto.class);
	}

	public Optional<Cliente> buscarPorNumeroConta(String numeroConta) {
		log.info("Buscando Cliente pelo Numero da Conta {}", numeroConta);
		return Optional.ofNullable(this.repository.findByNumeroConta(numeroConta));
	}

}
