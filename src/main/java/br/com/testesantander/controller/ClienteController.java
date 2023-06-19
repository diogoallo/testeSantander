package br.com.testesantander.controller;

import br.com.testesantander.dtos.ClienteDto;
import br.com.testesantander.dtos.SaqueDto;
import br.com.testesantander.model.Cliente;
import br.com.testesantander.services.ClienteServices;
import br.com.testesantander.util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cliente/")
public class ClienteController {

    @Autowired
    ClienteServices clienteServices;

    @Value("${paginacao.qtd_por_pagina}")
    private int qtdPorPagina;

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);


    @PostMapping()
    public ResponseEntity<Message> salva(@RequestBody ClienteDto clienteDto) throws Exception {
        Message retorno = new Message();
        try {
            Optional<Cliente> cliente = clienteServices.buscarPorNumeroConta(clienteDto.getNumeroConta());
            if (cliente.isPresent()) {
                retorno.setMessage("Cliente " + clienteDto.getNome() +" já Cadastrado");
            }
            clienteServices.create(clienteDto);
            retorno.setMessage("Cliente Cadastrado com Sucesso!!");
        } catch (Exception ex) {
            log.error("Erro --> " + ex.getMessage());
            return new ResponseEntity<Message>(retorno, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Message>(retorno,HttpStatus.OK);
    }

    @PutMapping("/saque/")
    public ResponseEntity<Message> saqueValor(@RequestBody SaqueDto saqueDto) throws Exception {
        Message retorno = new Message();
        try {
            Optional<Cliente> cliente = clienteServices.buscarPorNumeroConta(saqueDto.getNumeroConta());
            if (!cliente.isPresent()) {
                retorno.setMessage("Cliente não cadastrado!");
                return new ResponseEntity<Message>(retorno, HttpStatus.BAD_REQUEST);
            }

            Double valorSaque = saqueDto.getValorSaque().doubleValue();
            Double valorSaldoComTaxa = this.calculaTaxaSaque(valorSaque, cliente.get().getSaldo().doubleValue()
                    ,cliente.get().getPlanoExclusive());

            ClienteDto clienteDto = this.converteDto(cliente, valorSaldoComTaxa);

            clienteServices.create(clienteDto);
            retorno.setMessage("Saque Realizado com sucesso!!");

        } catch (Exception ex) {
            log.error("Erro --> " + ex.getMessage());
            retorno.setMessage(ex.getMessage());
            return new ResponseEntity<Message>(retorno, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Message>(retorno,HttpStatus.OK);
    }


    @GetMapping()
    public ResponseEntity<List<ClienteDto>> consultaClientes() throws Exception {
        List<ClienteDto> dados = new ArrayList<ClienteDto>();
        try {
            dados = clienteServices.findAll();
        }
        catch (Exception ex) {
            log.error("Erro --> " + ex.getMessage());
            return new ResponseEntity<List<ClienteDto>>(dados,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<ClienteDto>>(dados,HttpStatus.OK);
    }



    private Double calculaTaxaSaque(Double valorSaque, Double saldoAtual, Boolean planoExclusive){

        Double valorPercentual, valorTaxa;
        Double valorSaldo = null;

        if(!planoExclusive){
            if(valorSaque <= 100.00){
                valorSaldo = saldoAtual - valorSaque;
            } else if ((valorSaque > 100.00)||(valorSaque <= 300.00)) {
                valorPercentual = 0.4 / 100;
                valorTaxa = valorSaque + (valorPercentual * valorSaque);
                valorSaldo = saldoAtual - valorTaxa;
            }  else if (valorSaque > 300.00) {
                valorPercentual = 1.0 / 100;
                valorTaxa = valorSaque + (valorPercentual * valorSaque);
                valorSaldo = saldoAtual - valorTaxa;
            }
        }else{
            valorSaldo = saldoAtual - valorSaque;
        }
        return valorSaldo;
    }

    private void verificaClienteExistente(String numeroConta, BindingResult result){
        Optional<Cliente> cliente = clienteServices.buscarPorNumeroConta(numeroConta);
        if (cliente.isPresent()) {
            result.addError(new ObjectError("cliente", " Cliente já Cadastrado!"));
        }
    }

    private ClienteDto converteDto(Optional<Cliente> cliente, Double saldoAtual) {
        ClienteDto clienteDto = new ClienteDto();

        clienteDto.setId(cliente.get().getId());
        clienteDto.setNome(cliente.get().getNome());
        clienteDto.setPlanoExclusive(cliente.get().getPlanoExclusive());
        clienteDto.setSaldo(BigDecimal.valueOf(saldoAtual));
        clienteDto.setNumeroConta(cliente.get().getNumeroConta());
        clienteDto.setDataNascimento(cliente.get().getDataNascimento());

        return clienteDto;
    }
}
