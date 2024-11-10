package sistema.podio;

import sistema.usuario.Usuario;

public class Podio {
	private Usuario inquilino;
	private long cantidadReservas;

	public Podio(Usuario inquilino, long cantidadReservas) {
		this.inquilino = inquilino;
		this.cantidadReservas = cantidadReservas;
	}

	public Usuario getInquilino() {
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
