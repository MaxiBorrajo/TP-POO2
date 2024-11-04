package sistema.notificaciones;

import sistema.alquiler.Alquiler;

public abstract class EventoNotificador {
	private Alquiler alq;
	
	public EventoNotificador(Alquiler alq) {
		this.alq = alq;
	}
	
	protected Alquiler getAlquiler() {
		return this.alq;
	}
	
	public void notificarEspecifica(Suscriptor sus) {
		sus.update(this);
	}

	public final boolean esIgualA(EventoNotificador even2) {
		// TODO Auto-generated method stub
		return even2.esElMismoInmueble(this.alq) & this.esElMismoEvento(even2);
	}

	private boolean esElMismoInmueble(Alquiler alq) {
		// TODO Auto-generated method stub
		return this.alq.tienenElMismoInmueble(alq);
	}

	protected abstract boolean esElMismoEvento(EventoNotificador even2);

	public boolean esCancelacion() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean esReserva() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean esBajaDePrecio() {
		// TODO Auto-generated method stub
		return false;
	}
}
