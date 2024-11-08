package sistema.managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;
import sistema.alquiler.politicaDeCancelacion.PoliticaDeCancelacion;
import sistema.enums.EstadoDeReserva;
import sistema.enums.FormaDePago;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.NoExistenteException;
import sistema.reserva.Reserva;
import sistema.usuario.*;

public class ReservaManagerTest {
	private ReservaManager reservaManager;
	private Usuario inquilino;
	private Alquiler alquiler;
	private FormaDePago formaDePago;
	private LocalDate entrada, salida;
	private Reserva reservaMock;

	@BeforeEach
	void setUp() throws Exception {
		this.reservaManager = new ReservaManager();
		this.inquilino = mock(Usuario.class);
		this.alquiler = mock(Alquiler.class);
		this.formaDePago = mock(FormaDePago.class);
		this.entrada = LocalDate.of(2023, 1, 1);
		this.salida = LocalDate.of(2023, 1, 10);
		this.reservaMock = mock(Reserva.class);
		Inmueble inmuebleMock = mock(Inmueble.class);
		when(alquiler.getInmueble()).thenReturn(inmuebleMock);
	}

	@Test
	public void unInquilinoPuedeCrearUnaReservaValida() throws Exception {

		when(alquiler.validateFormaDePago(formaDePago)).thenReturn(true);
		when(alquiler.puedeCrearReserva(entrada, salida)).thenReturn(true);

		Reserva reserva = reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino);

		assertNotNull(reserva);
	}

	@Test
	public void UnInquilinoNoPuedeCrearReservaConFormaDePagoNoAceptada() {

		when(alquiler.validateFormaDePago(formaDePago)).thenReturn(false);
		when(alquiler.puedeCrearReserva(entrada, salida)).thenReturn(true);

		assertThrows(FormaDePagoNoAceptadaException.class, () -> {
			reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino);
		});
	}

	@Test
	public void testCancelarReserva() throws Exception {
		when(reservaMock.estaActiva()).thenReturn(true);
		when(reservaMock.getAlquiler()).thenReturn(alquiler);
		NotificadorManager n = mock(NotificadorManager.class);

		reservaManager.getReservas().add(reservaMock);

		reservaManager.cancelarReserva(reservaMock, n);

		verify(reservaMock, times(1)).cancelar(reservaManager, n);
	}

	@Test
	public void testCrearReservaEncolaSiExisteConflictoDeFechas() throws Exception {

		Inmueble inmueble = mock(Inmueble.class);
		PoliticaDeCancelacion politicaDeCancelacion = mock(PoliticaDeCancelacion.class);
		Alquiler alquiler = new Alquiler(inmueble, LocalTime.of(14, 0), LocalTime.of(10, 0), 100.0,
				politicaDeCancelacion);

		alquiler.agregarFormaDePago(formaDePago);
		assertTrue(alquiler.puedeCrearReserva(entrada, salida));
		assertTrue(alquiler.puedeCrearReserva(entrada, salida));
		Reserva reservaActiva = reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino);
		assertTrue(reservaManager.getReservas().contains(reservaActiva));
		assertFalse(alquiler.hayReservasEncoladas());
		reservaManager.aceptarReserva(reservaActiva, mock(NotificadorManager.class));
		Reserva reserva = reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino);

		assertTrue(alquiler.hayReservasEncoladas());
	}

	@Test
	public void testDesencolarReservaAlCancelar() throws Exception {
		when(alquiler.validateFormaDePago(formaDePago)).thenReturn(true);
		when(alquiler.puedeCrearReserva(entrada, salida)).thenReturn(true);

		Reserva reservaActiva = reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino);
		reservaManager.aceptarReserva(reservaActiva, mock(NotificadorManager.class));
		assertTrue(reservaManager.getReservas().contains(reservaActiva));

		Reserva reservaEncolada = new Reserva(formaDePago, entrada, salida, alquiler, inquilino, 100);

		when(alquiler.hayReservasEncoladas()).thenReturn(true);

		when(alquiler.obtenerPrimeroDeReservasEncoladas()).thenReturn(reservaEncolada);

		reservaManager.cancelarReserva(reservaActiva, mock(NotificadorManager.class));

		Reserva nuevaReserva = reservaManager.getReservas().stream()
				.filter(r -> r.getFormaDepago().equals(reservaEncolada.getFormaDepago())
						&& r.getFechaInicio().equals(reservaEncolada.getFechaInicio())
						&& r.getFechaFinal().equals(reservaEncolada.getFechaFinal())
						&& r.getInquilino().equals(reservaEncolada.getInquilino()))
				.findFirst().orElse(null);

		assertNotNull(nuevaReserva);
	}

	@Test
	public void testCancelarReservaNoExistente() {
		assertThrows(NoExistenteException.class, () -> {
			reservaManager.cancelarReserva(reservaMock, mock(NotificadorManager.class));
		});
	}

	@Test
	public void testGetReservasFiltraCanceladasYFinalizadas() throws Exception {
		Inmueble inmuebleMock = mock(Inmueble.class);
		when(alquiler.getInmueble()).thenReturn(inmuebleMock);

		when(alquiler.validateFormaDePago(formaDePago)).thenReturn(true);

		when(alquiler.puedeCrearReserva(entrada, salida)).thenReturn(true);
		when(alquiler.puedeCrearReserva(entrada.plusDays(10), salida.plusDays(10))).thenReturn(true);
		when(alquiler.puedeCrearReserva(entrada.plusDays(20), salida.plusDays(20))).thenReturn(true);

		Reserva reserva1 = reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino);
		Reserva reserva2 = reservaManager.crearReserva(formaDePago, entrada.plusDays(10), salida.plusDays(10), alquiler,
				inquilino);
		Reserva reserva3 = reservaManager.crearReserva(formaDePago, entrada.plusDays(20), salida.plusDays(20), alquiler,
				inquilino);

		assertTrue(reservaManager.getReservas().contains(reserva2),
				"La reserva 2 debería estar en la lista antes de cancelar.");
		reservaManager.aceptarReserva(reserva2, mock(NotificadorManager.class));
		reservaManager.aceptarReserva(reserva3, mock(NotificadorManager.class));
		reservaManager.aceptarReserva(reserva1, mock(NotificadorManager.class));

		reservaManager.cancelarReserva(reserva2, mock(NotificadorManager.class));

		reservaManager.finalizarReserva(reserva3);

		List<Reserva> resultado = reservaManager.getReservasActivas();

		assertEquals(1, resultado.size());
		assertTrue(resultado.contains(reserva1));
		assertFalse(resultado.contains(reserva3));
		assertFalse(resultado.contains(reserva2));
	}

	@Test
	public void testTodasLasCiudades() {
		Usuario usuario = mock(Usuario.class);
		Reserva reserva1 = mock(Reserva.class);
		Reserva reserva2 = mock(Reserva.class);

		when(reserva1.getInquilino()).thenReturn(usuario);
		when(reserva2.getInquilino()).thenReturn(usuario);
		when(reserva1.esDeUsuario(usuario)).thenReturn(true);
		when(reserva2.esDeUsuario(usuario)).thenReturn(true);

		when(reserva1.getCiudad()).thenReturn("Ciudad A");
		when(reserva2.getCiudad()).thenReturn("Ciudad B");

		reservaManager.getReservas().addAll(Arrays.asList(reserva1, reserva2));

		List<String> ciudades = reservaManager.todasLasCiudades(usuario);

		assertEquals(2, ciudades.size());
		assertTrue(ciudades.contains("Ciudad A"));
		assertTrue(ciudades.contains("Ciudad B"));
	}
}
