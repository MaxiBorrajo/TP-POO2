package sistema.filtro;

import java.time.LocalDate;

import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

public class FiltroReservasFuturas extends FiltroReserva{

	public FiltroReservasFuturas(Usuario us) {
		super(us);
		this.agregarUnFiltro(new FiltroSimple<Reserva>(r -> r.fecahPosteriorA(LocalDate.now())));
		// TODO Auto-generated constructor stub
	}
	
}
