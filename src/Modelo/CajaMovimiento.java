package Modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class CajaMovimiento {

    private int id;
    private int idApertura;
    private LocalDate fecha;
    private LocalTime hora;
    private String tipo; // ENTRADA / SALIDA
    private BigDecimal monto;
    private String descripcion;
    private String usuario;

    // Solo para mostrar (NO persistir)
    private BigDecimal acumulado;

    public CajaMovimiento() {}

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdApertura() { return idApertura; }
    public void setIdApertura(int idApertura) { this.idApertura = idApertura; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public BigDecimal getAcumulado() { return acumulado; }
    public void setAcumulado(BigDecimal acumulado) { this.acumulado = acumulado; }
}
