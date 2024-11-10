package sistema.notificaciones;

import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;

public class ReservaNotify extends EventoNotificador {

	public ReservaNotify(Alquiler alq) {
		super(alq);
	}

	@Override
	public boolean esElMismoEvento(EventoNotificador even2) {
		return even2.esReserva();
	}

	@Override
	public boolean esReserva() {
		return true;
	}

}
