
package Modelo;

import java.math.BigDecimal;

/**
 *
 * @author feder
 */
public class Config {
    
    private int id;
    private String nombre;
    private String cuit;
    private String telefono;
    private String direccion;
    private String razon;
    private String entrada;
    private String salida;
    private BigDecimal efectivo;
    private BigDecimal transferencia;
    private BigDecimal credito;


    public Config() {
    }

    public Config(int id, String nombre, String cuit, String telefono, String direccion, String razon, String entrada, String Salida, BigDecimal efectivo, BigDecimal transferencia, BigDecimal credito) {
        this.id = id;
        this.nombre = nombre;
        this.cuit = cuit;
        this.telefono = telefono;
        this.direccion = direccion;
        this.razon = razon;
        this.entrada = entrada;
        this.salida = Salida;
        this.efectivo = efectivo;
        this.transferencia = transferencia;
        this.credito = credito;
    }
    
   
    
   





     public BigDecimal getEfectivo() { return efectivo; }

    public void setEfectivo(BigDecimal efectivo) { this.efectivo = efectivo; }

    public BigDecimal getTransferencia() { return transferencia; }

    public void setTransferencia(BigDecimal transferencia) { this.transferencia = transferencia; }

    public BigDecimal getCredito() { return credito; }

    public void setCredito(BigDecimal credito) { this.credito = credito; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }
    
   
    
}
