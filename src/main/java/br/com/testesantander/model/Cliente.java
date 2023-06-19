package br.com.testesantander.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @Column(name = "plano_exclusive")
    private Boolean planoExclusive;

    @Column(name = "saldo")
    private BigDecimal saldo;

    @Column(name = "numero_conta")
    private String numeroConta;

    @Column(name = "data_nascimento")
    private Date dataNascimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getPlanoExclusive() {
        return planoExclusive;
    }

    public void setPlanoExclusive(Boolean planoExclusive) {
        this.planoExclusive = planoExclusive;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id) && Objects.equals(nome, cliente.nome) &&
                Objects.equals(planoExclusive, cliente.planoExclusive) && Objects.equals(saldo, cliente.saldo)
                && Objects.equals(numeroConta, cliente.numeroConta) && Objects.equals(dataNascimento, cliente.dataNascimento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, planoExclusive, saldo, numeroConta, dataNascimento);
    }
}
