package Modelo;

import java.math.BigDecimal;

public class Productos {

    private int id;
    private String codigo;
    private String descripcion;
    private BigDecimal precioCompra; 
    private BigDecimal precio;
    private BigDecimal stock;
    private String proveedor;
    private Integer idCategoria;
    private Integer idTalle;
    private Integer idColor;
    private String categoriaNombre;
    private String talleNombre;
    private String colorNombre;
   // public String codigoBarras; // opcional pero útil


    public Productos() {
    }
    
    public Productos(int id, String descripcion, BigDecimal precio) {
    this.id = id;
    this.descripcion = descripcion;
    this.precio = precio;
    }


    public Productos(int id, String codigo, String descripcion, BigDecimal precio,
                     BigDecimal stock, String proveedor, int categoria, int talle,
                     int color, String catNombre, String talleNombre, String colorNombre, BigDecimal precioCompra ) {
        
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.proveedor = proveedor;
        this.idCategoria = categoria;
        this.idTalle = talle;
        this.idColor = color;
        this.categoriaNombre = catNombre;
        this.talleNombre = talleNombre;
        this.colorNombre = colorNombre;
        this.precioCompra = precioCompra;
    }
    
    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public String getTalleNombre() {
        return talleNombre;
    }

    public void setTalleNombre(String talleNombre) {
        this.talleNombre = talleNombre;
    }

    public String getColorNombre() {
        return colorNombre;
    }

    public void setColorNombre(String colorNombre) {
        this.colorNombre = colorNombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }
    
    /*Desde aca en adelante las categorias, talles, colores y precioCompra*/
    
    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdTalle() {
        return idTalle;
    }

    public void setIdTalle(int idTalle) {
        this.idTalle = idTalle;
    }

    public int getIdColor() {
        return idColor;
    }

    public void setIdColor(int idColor) {
        this.idColor = idColor;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    
}
