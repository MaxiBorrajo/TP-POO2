package sistema.reserva;

import sistema.managers.NotificadorManager;

public class Pendiente extends EstadoReserva {
	public void aceptar(Reserva re, NotificadorManager noti) {
		re.aceptarReserva(noti);

	}

	public void rechazar(Reserva re) {
		re.setEstado(new Rechazada());
	}

}
