package sistema.reserva;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.alquiler.Alquiler;
import sistema.enums.FormaDePago;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.NoExistenteException;
import sistema.exceptions.ReservaNoCancelableException;
import sistema.managers.NotificadorManager;
import sistema.managers.ReservaManager;
import sistema.notificaciones.CancelacionNotify;
import sistema.notificaciones.ReservaNotify;
import sistema.usuario.Usuario;

public class ReservaTest {
	private Reserva re;
	private Usuario us;
	private Alquiler alq;

	@BeforeEach
	public void setUp() {
		this.us = mock(Usuario.class);
		this.alq = mock(Alquiler.class);
		this.re = new Reserva(FormaDePago.EFECTIVO, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 2, 2), this.alq,
				this.us, 2500);
	}

	@Test
	public void testDeGetters() {
		assertEquals(this.us, this.re.getInquilino());
		assertEquals(this.alq, this.re.getAlquiler());
		assertEquals(LocalDate.of(2024, 1, 1), this.re.getFechaInicio());
		assertEquals(LocalDate.of(2024, 2, 2), this.re.getFechaFinal());
		assertEquals(FormaDePago.EFECTIVO, this.re.getFormaDepago());
	}

	@Test
	public void testSabeDecirSiEsDeUnUsuario() {
		assertTrue(this.re.esDeUsuario(us));
		assertFalse(this.re.esDeUsuario(mock(Usuario.class)));
	}

	@Test
	public void testSabeCompararSUsFechas() {
		assertTrue(this.re.fechaPosteriorAInicio(LocalDate.of(2024, 2, 2)));
		assertFalse(this.re.fechaPosteriorAInicio(LocalDate.of(2023, 2, 2)));
		assertTrue(this.re.fechaPosteriorAFinal(LocalDate.of(2025, 1, 1)));
		assertFalse(this.re.fechaPosteriorAFinal(LocalDate.of(2023, 1, 1)));
	}

	@Test
	public void testSabeDeQueCiudadEs() {
		when(this.alq.esDeCiudad("Quilmes")).thenReturn(true);
		when(this.alq.getCiudad()).thenReturn("Quilmes");
		assertTrue(this.re.esDeCiudad("Quilmes"));
		assertEquals(this.re.getCiudad(), "Quilmes");
	}

	@Test
	public void testSePuedeAceptarUnaReservaRecienCreada() {
		NotificadorManager noti = mock(NotificadorManager.class);
		this.re.aceptar(noti);

		assertTrue(this.re.estaActiva());
		verify(noti).notify(any(ReservaNotify.class));
		verify(this.alq).seAceptoReserva(re);
	}

	@Test
	public void testSePuedeRechazarUnaReservaRecienCreada() {

		this.re.rechazar();

		assertTrue(this.re.estaRechazada());
		assertFalse(this.re.estaFinalizada());

	}

	@Test
	public void testSePuedeCancelarUnaReservaYaAceptada()
			throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException, ReservaNoCancelableException {
		ReservaManager reser = mock(ReservaManager.class);
		NotificadorManager noti = mock(NotificadorManager.class);
		this.re.aceptar(noti);
		this.re.cancelar(reser, noti);

		assertFalse(this.re.estaActiva());
		verify(noti).notify(any(CancelacionNotify.class));
		verify(this.alq).seCanceloReserva(re, reser);

	}

	@Test
	public void testSePuedeFinalizarUnaReserva()
			throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException, ReservaNoCancelableException {
		this.re.aceptar(mock(NotificadorManager.class));
		this.re.finalizar();
		assertFalse(this.re.estaActiva());
		assertFalse(this.re.estaRechazada());
		assertTrue(this.re.estaFinalizada());

	}

	@Test
	public void testSiEstaPendienteNoSePuedeeCancelarFInaliza() {
		this.re.finalizar();

		assertFalse(this.re.estaFinalizada());

	}

	@Test
	public void testUnaVezFinalizadaNoSePuedeAceptarNiRechazar()
			throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException, ReservaNoCancelableException {
		this.re.aceptar(mock(NotificadorManager.class));
		this.re.finalizar();
		this.re.aceptar(mock(NotificadorManager.class));
		assertFalse(this.re.estaActiva());
		this.re.rechazar();
		assertFalse(this.re.estaRechazada());

		assertTrue(this.re.estaFinalizada());

	}

	@Test
	public void testCancelarCunadoNoSePuedeTiraExcepcion()
			throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException, ReservaNoCancelableException {

		assertThrows(ReservaNoCancelableException.class, () -> {
			this.re.cancelar(mock(ReservaManager.class), mock(NotificadorManager.class));
		});
	}

}
