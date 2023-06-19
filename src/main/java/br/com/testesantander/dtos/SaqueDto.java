package br.com.testesantander.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@JsonPropertyOrder({"numeroConta","valorSaque"})
public class SaqueDto implements Serializable {

    private String numeroConta;
    private BigDecimal valorSaque;

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public BigDecimal getValorSaque() {
        return valorSaque;
    }

    public void setValorSaque(BigDecimal valorSaque) {
        this.valorSaque = valorSaque;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaqueDto saqueDto = (SaqueDto) o;
        return Objects.equals(numeroConta, saqueDto.numeroConta) && Objects.equals(valorSaque, saqueDto.valorSaque);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroConta, valorSaque);
    }
}
