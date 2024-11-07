package sistema.usuario;

import sistema.enums.RolDeUsuario;
import sistema.ranking.Ranking;
import sistema.reserva.Reserva;

public class Inquilino extends Usuario {

	public Inquilino(String nombre, String mail, String telefono) {
		super(nombre, mail, telefono);
		this.rol = RolDeUsuario.INQUILINO;
	}

	@Override
	protected boolean esValoracionValida(Usuario ranker, Reserva reserva) {
		return reserva.getAlquiler().getInmueble().getPropietario().equals(ranker)
				&& reserva.getInquilino().equals(this);
	}

}
