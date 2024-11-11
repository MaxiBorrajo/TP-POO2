package sistema.notificaciones;

import sistema.alquiler.Alquiler;

public class BajaPrecioNotify extends EventoNotificador {

	public BajaPrecioNotify(Observable alq) {
		super(alq);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean esElMismoEvento(EventoNotificador even2) {
		// TODO Auto-generated method stub
		return even2.esBajaDePrecio();
	}
 
	@Override
	public boolean esBajaDePrecio() {
		return true;
	}

	@Override
	public void notificarEspecifica(Suscriptor sus) {
		Alquiler alq = this.getAlquiler();
		sus.publish("No te pierdas esta oferta: Un inmueble " + alq.getTipoDeInmueble() + " a tan solo "
				+ alq.getPrecioBase() + " pesos ");
	}

}
