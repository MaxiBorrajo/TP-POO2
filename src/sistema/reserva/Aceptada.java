package sistema.reserva;

import java.time.LocalDate;

import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.ReservaNoCancelableException;
import sistema.managers.NotificadorManager;
import sistema.managers.ReservaManager;

public class Aceptada extends EstadoReserva {

	public void cancelar(Reserva re, ReservaManager reser, NotificadorManager noti)
			throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException, ReservaNoCancelableException {
		
		re.cancelarReserva(reser, noti);
	}

	public void finalizar(Reserva re) {
		re.setEstado(new Finalizada());
	}

	public boolean estaAceptada() {
		return true;
	}
}
