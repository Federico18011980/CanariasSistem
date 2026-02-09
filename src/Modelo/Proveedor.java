
package Modelo;

/**
 *
 * @author feder
 */
public class Proveedor {
   private int id;
   private String cuit;
   private String nombre;
   private String telefono;
   private String domicilio;
   private String email;

    public Proveedor() {
    }

    public Proveedor(int id, String cuit, String nombre, String telefono, String domicilio, String email) {
        this.id = id;
        this.cuit = cuit;
        this.nombre = nombre;
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
   
   
}
