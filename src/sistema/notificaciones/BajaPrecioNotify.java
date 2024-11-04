package sistema.notificaciones;

import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;

public class BajaPrecioNotify extends EventoNotificador{

	public BajaPrecioNotify(Alquiler alq) {
		super(alq);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean esElMismoEvento(EventoNotificador even2) {
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
		sus.popUp("No te pierdas esta oferta: Un inmueble"+ alq.getTipoDeInmueble() + "a tan solo" + alq.getPrecioBase(),"ROJO",24);
	}
	
}
