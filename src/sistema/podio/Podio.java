package sistema.podio;

import sistema.usuario.Inquilino;

public class Podio {
    private Inquilino inquilino;
    private long cantidadReservas;

    public Podio(Inquilino inquilino, long cantidadReservas) {
        this.inquilino = inquilino;
        this.cantidadReservas = cantidadReservas;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public long getCantidadReservas() {
        return cantidadReservas;
    }

    @Override
    public String toString() {
        return "Inquilino: " + inquilino + ", Cantidad de Reservas: " + cantidadReservas;
    }
}
