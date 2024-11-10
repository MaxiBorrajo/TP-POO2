package sistema.filtro;

import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

public class FiltroReservaCiudad extends FiltroReserva {

	public FiltroReservaCiudad(Usuario us, String ciudad) {
		super(us);
		this.agregarUnFiltro(new FiltroSimple<Reserva>(r -> r.esDeCiudad(ciudad)));
		// TODO Auto-generated constructor stub
	}

}
