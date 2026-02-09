
package Modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class CajaCierre {

    private int id;
    private int idApertura;
    private LocalDate fecha;
    private LocalTime horaCierre;

    private BigDecimal montoFinal;
    private BigDecimal totalEfectivo;
    private BigDecimal totalTransferencia;
    private BigDecimal totalCredito;
    private BigDecimal totalSalidas;
    private BigDecimal diferencia;

    private String usuario;

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdApertura() { return idApertura; }
    public void setIdApertura(int idApertura) { this.idApertura = idApertura; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraCierre() { return horaCierre; }
    public void setHoraCierre(LocalTime horaCierre) { this.horaCierre = horaCierre; }

    public BigDecimal getMontoFinal() { return montoFinal; }
    public void setMontoFinal(BigDecimal montoFinal) { this.montoFinal = montoFinal; }

    public BigDecimal getTotalEfectivo() { return totalEfectivo; }
    public void setTotalEfectivo(BigDecimal totalEfectivo) { this.totalEfectivo = totalEfectivo; }

    public BigDecimal getTotalTransferencia() { return totalTransferencia; }
    public void setTotalTransferencia(BigDecimal totalTransferencia) { this.totalTransferencia = totalTransferencia; }

    public BigDecimal getTotalCredito() { return totalCredito; }
    public void setTotalCredito(BigDecimal totalCredito) { this.totalCredito = totalCredito; }

    public BigDecimal getTotalSalidas() { return totalSalidas; }
    public void setTotalSalidas(BigDecimal totalSalidas) { this.totalSalidas = totalSalidas; }

    public BigDecimal getDiferencia() { return diferencia; }
    public void setDiferencia(BigDecimal diferencia) { this.diferencia = diferencia; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
}
