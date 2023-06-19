package br.com.testesantander.services;

import br.com.testesantander.model.Cliente;
import br.com.testesantander.repository.ClienteRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ClienteServicesTest {

    @MockBean
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteServices clienteServices;

    private static final String NUMERO_CONTA = "53819-7";

    @Before
    public void setUp() throws Exception {
        BDDMockito.given(this.clienteRepository.save(Mockito.any(Cliente.class))).willReturn(new Cliente());
        BDDMockito.given(this.clienteRepository.findByNumeroConta(Mockito.anyString())).willReturn(new Cliente());
    }

    @Test
    public void testBuscarClienteNumeroDaConta() {
        Optional<Cliente> cliente = this.clienteServices.buscarPorNumeroConta(NUMERO_CONTA);
        assertTrue(cliente.isPresent());
    }

}
