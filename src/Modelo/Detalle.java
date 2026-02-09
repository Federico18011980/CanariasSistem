package Modelo;

import java.math.BigDecimal;

public class Detalle {
    private int id;
    private String cod_pro;
    private BigDecimal cant;
    private BigDecimal precio;
    private int idVenta;
    
    public Detalle(){}

    public Detalle(int id, String cod_pro, BigDecimal cant, BigDecimal precio, int idVenta) {
        this.id = id;
        this.cod_pro = cod_pro;
        this.cant = cant;
        this.precio = precio;
        this.idVenta = idVenta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCod_pro() {
        return cod_pro;
    }

    public void setCod_pro(String cod_pro) {
        this.cod_pro = cod_pro;
    }

    public BigDecimal getCant() {
        return cant;
    }

    public void setCant(BigDecimal cant) {
        this.cant = cant;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }
}
