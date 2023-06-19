package br.com.testesantander.repositories;

import br.com.testesantander.dtos.ClienteDto;
import br.com.testesantander.model.Cliente;
import br.com.testesantander.repository.ClienteRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    private static final BigDecimal VALOR_SALDO = BigDecimal.valueOf(2000.00);
    private static final String NUMERO_CONTA = "53819-7";

    @Before
    public void setUp() throws Exception {
        this.clienteRepository.save(obterDadosClente());
    }

    @Test
    public void testBuscarClientePorNumeroConta() {
        Cliente cliente = this.clienteRepository.findByNumeroConta(NUMERO_CONTA);
        assertNotNull(cliente);
    }

    @After
    public final void tearDown() {
        this.clienteRepository.deleteAll();
    }

    private Cliente obterDadosClente() throws NoSuchAlgorithmException {
        Cliente cliente = new Cliente();
        Date date = new Date();

        cliente.setNome("Allan da Silva");
        cliente.setPlanoExclusive(true);
        cliente.setSaldo(VALOR_SALDO);
        cliente.setNumeroConta("53819-7");
        cliente.setDataNascimento(date);

        return  cliente;
    }

}
