package Modelo;

public enum TipoPago {
    EFECTIVO,
    TRANSFERENCIA,
    CREDITO;

    @Override
    public String toString() {
        switch (this) {
            case EFECTIVO: return "Efectivo";
            case TRANSFERENCIA: return "Transferencia";
            case CREDITO: return "Crédito";
            default: return name();
        }
    }
}
