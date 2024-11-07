package sistema.usuario;

import sistema.enums.RolDeUsuario;
import sistema.reserva.Reserva;

public class Administrador extends Usuario {

	public Administrador(String nombreCompleto, String email, String telefono) {
		super(nombreCompleto, email, telefono);
		this.rol = RolDeUsuario.ADMINISTRADOR;
	}

	@Override
	protected boolean esValoracionValida(Usuario ranker, Reserva reserva) {
		// TODO Auto-generated method stub
		return false;
	}

}
