package sistema.reserva;

import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.ReservaNoCancelableException;
import sistema.managers.NotificadorManager;
import sistema.managers.ReservaManager;

public class EstadoReserva {
	public void aceptar(Reserva re, NotificadorManager noti) {
		
	}
	public void cancelar(Reserva re, ReservaManager reservaManager, NotificadorManager notificadorManager) throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException, ReservaNoCancelableException {
		throw new ReservaNoCancelableException();
	}
	public void rechazar(Reserva re) {
		
	}
	
	public void finalizar(Reserva rs) {
		
	}
	public boolean estaAceptada() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
