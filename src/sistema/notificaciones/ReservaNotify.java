package sistema.notificaciones;

import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;

public class ReservaNotify extends EventoNotificador {

	public ReservaNotify(Alquiler alq) {
		super(alq);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean esElMismoEvento(EventoNotificador even2) {
		// TODO Auto-generated method stub
		return even2.esReserva();
	}

	
	@Override
	public boolean esReserva() {
		return true;
	}
	
	
}
