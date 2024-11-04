package sistema.notificaciones;

import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;

public class CancelacionNotify extends EventoNotificador{

	public CancelacionNotify(Alquiler alq) {
		super(alq);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean esElMismoEvento(EventoNotificador even2) {
		// TODO Auto-generated method stub
		return even2.esCancelacion();
	}
	
	@Override
	public boolean esCancelacion() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	@Override
	public void notificarEspecifica(Suscriptor sus) {
		sus.publish("El/la" +this.getAlquiler().getTipoDeInmueble() + "que te interesa se ha liberado! Corre a reservarlo!");
	}
	
	

}
