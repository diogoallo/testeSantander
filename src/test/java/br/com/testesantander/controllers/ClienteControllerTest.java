package br.com.testesantander.controllers;


import br.com.testesantander.controller.ClienteController;
import br.com.testesantander.dtos.ClienteDto;
import br.com.testesantander.model.Cliente;
import br.com.testesantander.services.ClienteServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ClienteController.class)
@AutoConfigureMockMvc
public class ClienteControllerTest {

    private static final String URL_API = "/api/cliente/";
    private static final BigDecimal VALOR_SALDO = BigDecimal.valueOf(2000.00);
    private static final BigDecimal VALOR_SAQUE = BigDecimal.valueOf(189.25);
    private static final Date data = new Date();

    @Autowired
    MockMvc mvc;

    @MockBean
    ClienteServices clienteService;

    @Test
    public void criaClienteTest() throws Exception {
        Cliente cliente = obterDadosCliente();
        ClienteDto clienteDto = convertClienteParaClienteDto(cliente);

        BDDMockito.given(this.clienteService.buscarPorNumeroConta(Mockito.anyString())).willReturn(Optional.of(new Cliente()));
        BDDMockito.given(this.clienteService.create(Mockito.any(ClienteDto.class))).willReturn(clienteDto);

        mvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .content(this.obterJsonRequisicaoPost())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("10l"))
                .andExpect(jsonPath("$.nome").value("Allan da Silva"))
                .andExpect(jsonPath("$.planoExclusive").value(true))
                .andExpect(jsonPath("$.saldo").value(VALOR_SALDO))
                .andExpect(jsonPath("$.numeroConta").value("53819-9"))
                .andExpect(jsonPath("$.dataNascimento").value(data))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

   @Test
    public void testSaqueSaldoCliente() throws Exception {

       Double valorSaldoComTaxa = this.calculaTaxaSaque(VALOR_SAQUE.doubleValue(), VALOR_SALDO.doubleValue()
               , true);

       Cliente cliente = obterDadosClienteSaque(valorSaldoComTaxa);
       ClienteDto clienteDto = convertClienteParaClienteDto(cliente);

       BDDMockito.given(this.clienteService.buscarPorNumeroConta(Mockito.anyString())).willReturn(Optional.empty());
       BDDMockito.given(this.clienteService.create(Mockito.any(ClienteDto.class))).willReturn(clienteDto);


       mvc.perform(MockMvcRequestBuilders.get(URL_API + "/saque/")
                        .content(this.obterJsonSaqueGet(valorSaldoComTaxa))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value("10l"))
               .andExpect(jsonPath("$.nome").value("Allan da Silva"))
               .andExpect(jsonPath("$.planoExclusive").value(true))
               .andExpect(jsonPath("$.saldo").value(1810.75))
               .andExpect(jsonPath("$.numeroConta").value("53819-9"))
               .andExpect(jsonPath("$.dataNascimento").value(data))
               .andExpect(jsonPath("$.errors").isEmpty());

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


    private Cliente obterDadosCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("Allan da Silva");
        cliente.setPlanoExclusive(true);
        cliente.setSaldo(VALOR_SALDO);
        cliente.setNumeroConta("53819-8");
        cliente.setDataNascimento(data);

        return cliente;
    }

    private Cliente obterDadosClienteSaque(Double valorSaldoComTaxa) {
        Cliente cliente = new Cliente();
        cliente.setNome("Allan da Silva");
        cliente.setPlanoExclusive(true);
        cliente.setSaldo(BigDecimal.valueOf(valorSaldoComTaxa));
        cliente.setNumeroConta("53819-8");
        cliente.setDataNascimento(data);

        return cliente;
    }

    private String obterJsonRequisicaoPost() throws JsonProcessingException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNome("Allan da Silva");
        clienteDto.setPlanoExclusive(true);
        clienteDto.setSaldo(VALOR_SALDO);
        clienteDto.setNumeroConta("53819-8");
        clienteDto.setDataNascimento(data);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(clienteDto);
    }

    private String obterJsonSaqueGet(Double valorSaldoComTaxa) throws JsonProcessingException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNome("Allan da Silva");
        clienteDto.setPlanoExclusive(true);
        clienteDto.setSaldo(VALOR_SALDO);
        clienteDto.setNumeroConta("53819-8");
        clienteDto.setDataNascimento(data);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(clienteDto);
    }

    private ClienteDto convertClienteParaClienteDto(Cliente cliente){
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNome(cliente.getNome());
        clienteDto.setPlanoExclusive(cliente.getPlanoExclusive());
        clienteDto.setSaldo(cliente.getSaldo());
        clienteDto.setNumeroConta(cliente.getNumeroConta());
        clienteDto.setDataNascimento(data);

        return  clienteDto;
    }
}
