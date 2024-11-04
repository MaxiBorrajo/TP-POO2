package sistema.managers;

import sistema.alquiler.Alquiler;
import sistema.enums.FormaDePago;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaManager {
	private List<Reserva> reservas;

	public ReservaManager() {
		this.reservas = new ArrayList<>();
	}

	private void validarReserva(FormaDePago formaDePago, LocalDate entrada, LocalDate salida, Alquiler alquiler) throws FormaDePagoNoAceptadaException, AlquilerNoDisponibleException {
		if (!alquiler.validateFormaDePago(formaDePago)) {
			throw new FormaDePagoNoAceptadaException("Este alquiler no acepta esta forma de pago");
		}

		if (!alquiler.puedeCrearReserva(entrada, salida)) {
			throw new AlquilerNoDisponibleException("El alquiler no esta disponible en estas fechas");
		}
		
	}

	public Reserva crearReserva(FormaDePago formaDePago, LocalDate entrada, LocalDate salida, Alquiler alquiler,
			Usuario usuario) throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException {
		
		this.validarReserva(formaDePago, entrada, salida, alquiler);
		Reserva nuevaReserva = new Reserva(formaDePago, entrada, salida, alquiler, usuario);
		this.reservas.add(nuevaReserva);
		return nuevaReserva;

	}
}
