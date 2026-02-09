package Modelo;

import java.math.BigDecimal;

public class PreviewProducto {

   private String codigo;
private String descripcion;
private BigDecimal precio;
private int stock;
private String proveedor;
private String categoria;
private String talle;
private String color;


    public PreviewProducto(String codigo, String descripcion, BigDecimal precio,
                        int stock, String proveedor, String categoria,
                        String talle, String color) {

    this.codigo = codigo;
    this.descripcion = descripcion;
    this.precio = precio;
    this.stock = stock;
    this.proveedor = proveedor;
    this.categoria = categoria;
    this.talle = talle;
    this.color = color;
}


    

  public String getCodigo() { return codigo; }
public String getDescripcion() { return descripcion; }
public BigDecimal getPrecio() { return precio; }
public int getStock() { return stock; }
public String getProveedor() { return proveedor; }
public String getCategoria() { return categoria; }
public String getTalle() { return talle; }
public String getColor() { return color; }

}
