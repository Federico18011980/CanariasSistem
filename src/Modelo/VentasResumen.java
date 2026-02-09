package Modelo;

import java.math.BigDecimal;

public class VentasResumen {

    private BigDecimal efectivo = BigDecimal.ZERO;
    private BigDecimal transferencia = BigDecimal.ZERO;
    private BigDecimal credito = BigDecimal.ZERO;

    public BigDecimal getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(BigDecimal efectivo) {
        this.efectivo = efectivo;
    }

    public BigDecimal getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(BigDecimal transferencia) {
        this.transferencia = transferencia;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }
}
