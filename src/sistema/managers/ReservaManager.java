package sistema.managers;

import sistema.alquiler.Alquiler;
import sistema.enums.EstadoDeReserva;
import sistema.enums.FormaDePago;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.NoExistenteException;
import sistema.filtro.FiltroReserva;
import sistema.filtro.FiltroSimple;
import sistema.filtro.FiltroTodasReservas;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaManager {
	private List<Reserva> reservas;
	private NotificadorManager noti;
	public ReservaManager() {
		this.reservas = new ArrayList<>();
		
	}

	private void validarReserva(FormaDePago formaDePago, LocalDate entrada, LocalDate salida, Alquiler alquiler)
			throws FormaDePagoNoAceptadaException, AlquilerNoDisponibleException {
		if (!alquiler.validateFormaDePago(formaDePago)) {
			throw new FormaDePagoNoAceptadaException("Este alquiler no acepta esta forma de pago");
		}

		if (!alquiler.puedeCrearReserva(entrada, salida)) {
			throw new AlquilerNoDisponibleException("Alquiler no disponible para las fechas seleccionadas");
		}

	}

	public Reserva crearReserva(FormaDePago formaDePago, LocalDate entrada, LocalDate salida, Alquiler alquiler,
			Usuario usuario) throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException {

		this.validarReserva(formaDePago, entrada, salida, alquiler);

		Reserva nuevaReserva = new Reserva(formaDePago, entrada, salida, alquiler, usuario);

		if (this.hayReservaExistenteParaElPeriodoDado(alquiler, nuevaReserva)) {
			alquiler.encolarReserva(nuevaReserva);
		} else {
			this.reservas.add(nuevaReserva);
		}

		return nuevaReserva;

	}

	boolean hayReservaExistenteParaElPeriodoDado(Alquiler alquiler, Reserva reservaDeAlquiler) {
		for (Reserva reserva : this.getReservasActivas()) {
			if (reserva.getAlquiler().equals(alquiler) && fechasSeSolapan(reserva.getFechaInicio(),
					reserva.getFechaFinal(), reservaDeAlquiler.getFechaInicio(), reservaDeAlquiler.getFechaFinal())) {
				return true;
			}
		}
		return false;
	}

	private boolean fechasSeSolapan(LocalDate inicio1, LocalDate fin1, LocalDate inicio2, LocalDate fin2) {
		return !(fin1.isBefore(inicio2) || fin2.isBefore(inicio1));
	}

	public void cancelarReserva(Reserva reserva, NotificadorManager notificadorManager)
			throws NoExistenteException, AlquilerNoDisponibleException, FormaDePagoNoAceptadaException {
		this.validarReservaExiste(reserva);
		reserva.cancelar(this, notificadorManager);
		
	}
	
	public void desencoolarReserva(Alquiler alquiler) throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException {
		// TODO Auto-generated method stub
		Reserva reservaEncolada = alquiler.obtenerPrimeroDeReservasEncoladas();
		this.crearReserva(reservaEncolada.getFormaDepago(), reservaEncolada.getFechaInicio(),
				reservaEncolada.getFechaFinal(), alquiler, reservaEncolada.getInquilino());
	}

	public void finalizarReserva(Reserva reserva) throws NoExistenteException {
		this.validarReservaExiste(reserva);
		reserva.finalizar();
	}

	public List<Reserva> getReservasActivas() {
		List<Reserva> reservasActivas = new FiltroSimple<Reserva>(
				r -> r.estaActiva() )
				.filtrarLista(reservas);
		return reservasActivas;
	}
//r.getEstado() != EstadoDeReserva.CANCELADA && r.getEstado() != EstadoDeReserva.FINALIZADA
	public List<Reserva> getReservas() {
		return this.reservas;
	}

	private void validarReservaExiste(Reserva reserva) throws NoExistenteException {
		
		if (!this.getReservasActivas().contains(reserva)) {
			throw new NoExistenteException("Reserva");
		}

	}

	public List<Reserva> filtrarReservas(FiltroReserva f) {
		// TODO Auto-generated method stub
		return f.filtrarReservas(this.reservas);
	}

	public List<String> todasLasCiudades(Usuario user) {
		// TODO Auto-generated method stub

		return (new FiltroTodasReservas(user)).filtrarReservas(this.reservas).stream().map(s -> (String) s.getCiudad())
				.toList();
	}

	public void aceptarReserva(Reserva reser, NotificadorManager notificadorManager) {
		// TODO Auto-generated method stub
		reser.aceptar(notificadorManager);
	}

	public void rechazarReserva(Reserva reser) {
		// TODO Auto-generated method stub
		
	}

	
}
