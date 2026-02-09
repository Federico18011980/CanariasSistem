package Modelo;

public class ImportError {

    private int fila;
    private String columna;
    private String mensaje;

    public ImportError(int fila, String columna, String mensaje) {
        this.fila = fila;
        this.columna = columna;
        this.mensaje = mensaje;
    }

    ImportError(int filaNumero, String precio_inválido) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getFila() {
        return fila;
    }

    public String getColumna() {
        return columna;
    }

    public String getMensaje() {
        return mensaje;
    }
}
