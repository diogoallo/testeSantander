package br.com.testesantander.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ClienteDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private Boolean planoExclusive;
    private BigDecimal saldo;
    private String numeroConta;
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


}
