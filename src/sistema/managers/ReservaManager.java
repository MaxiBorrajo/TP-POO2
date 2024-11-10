package sistema.managers;

import sistema.alquiler.Alquiler;
import sistema.enums.FormaDePago;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.NoExistenteException;
import sistema.exceptions.PermisoDenegadoException;
import sistema.exceptions.ReservaNoCancelableException;
import sistema.filtro.FiltroReserva;
import sistema.filtro.FiltroSimple;
import sistema.filtro.FiltroTodasReservas;
import sistema.mailSender.MailSender;
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

	private void validarReserva(FormaDePago formaDePago, LocalDate entrada, LocalDate salida, Alquiler alquiler)
			throws FormaDePagoNoAceptadaException, AlquilerNoDisponibleException {
		if (!alquiler.validateFormaDePago(formaDePago)) {
			throw new FormaDePagoNoAceptadaException("Este alquiler no acepta esta forma de pago");
		}

	}

	public Reserva crearReserva(FormaDePago formaDePago, LocalDate entrada, LocalDate salida, Alquiler alquiler,
			Usuario usuario, boolean esReservaCondicional)
			throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException {

		this.validarReserva(formaDePago, entrada, salida, alquiler);

		double precioTotal = alquiler.calcularPrecioPeriodo(entrada, salida);

		Reserva nuevaReserva = new Reserva(formaDePago, entrada, salida, alquiler, usuario, precioTotal);

		if (!alquiler.puedeCrearReserva(entrada, salida) && !esReservaCondicional) {
			throw new AlquilerNoDisponibleException("Alquiler no disponible para su reserva");
		}

		if (!alquiler.puedeCrearReserva(entrada, salida) && esReservaCondicional) {
			alquiler.encolarReserva(nuevaReserva);
		}

		if (alquiler.puedeCrearReserva(entrada, salida)) {
			this.reservas.add(nuevaReserva);
		}

		return nuevaReserva;

	}

	public void cancelarReserva(Reserva reserva, Usuario inquilinoCancela, NotificadorManager notificadorManager,
			MailSender mailSender) throws NoExistenteException, AlquilerNoDisponibleException,
			FormaDePagoNoAceptadaException, ReservaNoCancelableException, PermisoDenegadoException {
		validarAccionSobreReserva(reserva, inquilinoCancela);
		reserva.cancelar(this, notificadorManager);
		mailSender.sendEmail("hola@sistema.com", reserva.getAlquiler().getInmueble().getPropietario().getEmail(),
				"Reserva cancelada", "El inquilino ha cancelado la reserva del alquiler");
	}

	private void verificarPermiso(Usuario usuarioEsperado, Usuario usuarioActual) throws PermisoDenegadoException {
		if (usuarioEsperado.equals(usuarioActual)) {
			throw new PermisoDenegadoException();
		}
	}

	public void desencolarReserva(Alquiler alquiler)
			throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException {
		Reserva reservaEncolada = alquiler.obtenerPrimeroDeReservasEncoladas();
		this.crearReserva(reservaEncolada.getFormaDepago(), reservaEncolada.getFechaInicio(),
				reservaEncolada.getFechaFinal(), alquiler, reservaEncolada.getInquilino(), true);
	}

	public void finalizarReserva(Reserva reserva) throws NoExistenteException {
		this.validarReservaExiste(reserva);
		reserva.finalizar();
	}

	public List<Reserva> getReservasActivas() {
		List<Reserva> reservasActivas = new FiltroSimple<Reserva>(r -> r.estaActiva()).filtrarLista(reservas);
		return reservasActivas;
	}

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

	public void aceptarReserva(Reserva reserva, Usuario propietarioAcepta, NotificadorManager notificadorManager,
			MailSender mailSender) throws NoExistenteException, PermisoDenegadoException {
		validarAccionSobreReserva(reserva, propietarioAcepta);
		reserva.aceptar(notificadorManager);
		mailSender.sendEmail("hola@sistema.com", reserva.getInquilino().getEmail(), "Reserva aprobada",
				"El propietario ha aceptado tu reserva del alquiler");
	}

	private void validarAccionSobreReserva(Reserva reserva, Usuario propietarioAcepta)
			throws NoExistenteException, PermisoDenegadoException {
		this.validarReservaExiste(reserva);
		this.verificarPermiso(reserva.getAlquiler().getInmueble().getPropietario(), propietarioAcepta);
	}

	public void rechazarReserva(Reserva reserva, Usuario propietarioRechaza)
			throws NoExistenteException, PermisoDenegadoException {
		validarAccionSobreReserva(reserva, propietarioRechaza);
	}

}
