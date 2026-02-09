package Modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class CajaApertura {

    private int id;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private BigDecimal montoInicial;
    private String usuario;
    private String estado; // ABIERTA / CERRADA

    public CajaApertura() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public BigDecimal getMontoInicial() { return montoInicial; }
    public void setMontoInicial(BigDecimal montoInicial) { this.montoInicial = montoInicial; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
