
package Modelo;

import java.math.BigDecimal;

/**
 *
 * @author feder
 */
public class Caja {
    private int id;
    private BigDecimal entrada;
    private BigDecimal salida;
    private BigDecimal monto; 
    private String fecha; 
    private String usuario;
    public Caja() {
    }

    public Caja(int id, BigDecimal entrada, BigDecimal salida, String fecha, String usuario) {
        this.id = id;
        this.entrada = entrada;
        this.salida = salida; 
        this.fecha = fecha;
        this.usuario = usuario;
        //this.monto = this.entrada - this.salida;
    }
    
//    public void monto(double montoAnterior){
//        this.monto = this.entrada - this.salida + montoAnterior;
//    }
    public void setMonto(BigDecimal monto){
        this.monto = monto;
    }
    
    public BigDecimal getMonto(){
        return this.monto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getEntrada() {
        return entrada;
    }

    public void setEntrada(BigDecimal entrada) {
        this.entrada = entrada;
    }

    public BigDecimal getSalida() {
        return salida;
    }

    public void setSalida(BigDecimal salida) {
        this.salida = salida;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    public void setUsuario(String usuario){this.usuario = usuario;}
    
    public String getUsuario(){return usuario;}
    
    
}
