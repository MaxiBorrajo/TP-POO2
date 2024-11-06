package sistema.reserva;

import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.managers.NotificadorManager;
import sistema.managers.ReservaManager;

public class Aceptada  extends EstadoReserva{
	
	public void  cancelar(Reserva re, ReservaManager reser, NotificadorManager noti ) throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException {
		re.cancelarReserva(reser, noti);
	}
	
	public void finalizar(Reserva re) {
		re.setEstado(new Finalizada());
	}
	
	public boolean estaAceptada() {
		return true;
	}
	
}
