package br.com.testesantander.repository;

import br.com.testesantander.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByNumeroConta(String numeroConta);

}
