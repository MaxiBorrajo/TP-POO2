package sistema.notificaciones;

import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;

public class CancelacionNotify extends EventoNotificador {

	public CancelacionNotify(Alquiler alq) {
		super(alq);
	}

	@Override
	public boolean esElMismoEvento(EventoNotificador even2) {
		return even2.esCancelacion();
	}

	@Override
	public boolean esCancelacion() {
		return true;
	}

	@Override
	public void notificarEspecifica(Suscriptor sus) {
		sus.popUp("El/la " + this.getAlquiler().getTipoDeInmueble()
				+ "que te interesa se ha liberado! Corre a reservarlo!", "ROJO", 24);
	}

}
