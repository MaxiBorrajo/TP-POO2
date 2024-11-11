package sistema.notificaciones;

import sistema.alquiler.Alquiler;

public abstract class EventoNotificador {
	private Observable alq;

	public EventoNotificador(Observable alq) {
		this.alq = alq;
	}

	protected Alquiler getAlquiler() {
		return this.alq.getAlquiler();
	}

	public void notificarEspecifica(Suscriptor sus) {
		sus.update(this);
	}

	public final boolean esIgualA(EventoNotificador even2) {
		return this.esElMismoEvento(even2) && even2.esElMismoObservable(this.alq);
	}

	public boolean esElMismoObservable(Observable alq2) {
		return this.alq.equals(alq2);
	}

	public abstract boolean esElMismoEvento(EventoNotificador even2);

	public boolean esCancelacion() {
		return false;
	}

	public boolean esReserva() {
		return false;
	}

	public boolean esBajaDePrecio() {
		return false;
	}
}
