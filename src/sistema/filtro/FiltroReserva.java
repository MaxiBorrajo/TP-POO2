package sistema.filtro;

import java.util.List;

import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

public abstract class FiltroReserva {
	private FiltroCompuesto<Reserva> filtro;

	public FiltroReserva(Usuario us) {
		this.filtro = new FiltroCompuesto<Reserva>();
		this.filtro.agregarFiltro(new FiltroSimple<Reserva>(r -> r.esDeUsuario(us)));
	}

	public List<Reserva> filtrarReservas(List<Reserva> xs) {
		return this.filtro.filtrarLista(xs);
	}

	protected void agregarUnFiltro(Filtro<Reserva> f) {
		this.filtro.agregarFiltro(f);
	}

}
