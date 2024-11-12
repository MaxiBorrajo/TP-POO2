package sistema.managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;
import sistema.alquiler.politicaDeCancelacion.PoliticaDeCancelacion;
import sistema.enums.FormaDePago;
import sistema.enums.RolDeUsuario;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.NoExistenteException;
import sistema.filtro.FiltroReserva;
import sistema.filtro.FiltroReservasFuturas;
import sistema.mailSender.MailSender;
import sistema.reserva.Reserva;
import sistema.usuario.*;

public class ReservaManagerTest {
	private ReservaManager reservaManager;
	private Usuario inquilino;
	private Usuario propietario;
	private Alquiler alquiler;
	private FormaDePago formaDePago;
	private LocalDate entrada, salida;
	private Reserva reservaMock;
	private MailSender mailSender;

	@BeforeEach
	void setUp() throws Exception {
		this.reservaManager = new ReservaManager();
		this.inquilino = mock(Usuario.class);
		this.propietario = mock(Usuario.class);
		this.alquiler = mock(Alquiler.class);
		this.formaDePago = mock(FormaDePago.class);
		this.entrada = LocalDate.of(2023, 1, 1);
		this.salida = LocalDate.of(2023, 1, 10);
		this.reservaMock = mock(Reserva.class);
		this.mailSender = mock(MailSender.class);
		Inmueble inmuebleMock = mock(Inmueble.class);
		when(alquiler.getInmueble()).thenReturn(inmuebleMock);
		when(inmuebleMock.getPropietario()).thenReturn(propietario);
		when(propietario.getRol()).thenReturn(RolDeUsuario.PROPIETARIO);
	}

	@Test
	public void unInquilinoPuedeCrearUnaReservaValida() throws Exception {

		when(alquiler.existeFormaDePago(formaDePago)).thenReturn(true);
		when(alquiler.puedeCrearReserva(entrada, salida)).thenReturn(true);

		Reserva reserva = reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino, false);

		assertNotNull(reserva);
	}

	@Test
	public void UnInquilinoNoPuedeCrearReservaConFormaDePagoNoAceptada() {

		when(alquiler.existeFormaDePago(formaDePago)).thenReturn(false);
		when(alquiler.puedeCrearReserva(entrada, salida)).thenReturn(true);

		assertThrows(FormaDePagoNoAceptadaException.class, () -> {
			reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino, false);
		});
	}

	@Test
	public void testCancelarReserva() throws Exception {
		when(reservaMock.estaActiva()).thenReturn(true);
		when(reservaMock.getAlquiler()).thenReturn(alquiler);
		when(reservaMock.getInquilino()).thenReturn(inquilino);
		NotificadorManager n = mock(NotificadorManager.class);

		reservaManager.getReservas().add(reservaMock);

		reservaManager.cancelarReserva(reservaMock, inquilino, n, mailSender);

		verify(reservaMock, times(1)).cancelar(reservaManager, n);
	}

	@Test
	public void testCrearReservaEncolaSiExisteConflictoDeFechas() throws Exception {

		Inmueble inmueble = mock(Inmueble.class);
		when(inmueble.getPropietario()).thenReturn(propietario);
		PoliticaDeCancelacion politicaDeCancelacion = mock(PoliticaDeCancelacion.class);
		Alquiler alquiler = new Alquiler(inmueble, LocalTime.of(14, 0), LocalTime.of(10, 0), 100.0,
				politicaDeCancelacion, Arrays.asList(formaDePago));

		assertTrue(alquiler.puedeCrearReserva(entrada, salida));
		assertTrue(alquiler.puedeCrearReserva(entrada, salida));
		Reserva reservaActiva = reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino, true);
		assertTrue(reservaManager.getReservas().contains(reservaActiva));
		assertFalse(alquiler.hayReservasEncoladas());
		reservaManager.aceptarReserva(reservaActiva, propietario, mock(NotificadorManager.class), mailSender);
		Reserva reserva = reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino, true);

		assertTrue(alquiler.hayReservasEncoladas());
	}

	@Test
	public void testDesencolarReservaAlCancelar() throws Exception {
		when(alquiler.existeFormaDePago(formaDePago)).thenReturn(true);
		when(alquiler.puedeCrearReserva(entrada, salida)).thenReturn(true);

		Reserva reservaActiva = reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino, true);
		reservaManager.aceptarReserva(reservaActiva, propietario, mock(NotificadorManager.class), mailSender);
		assertTrue(reservaManager.getReservas().contains(reservaActiva));

		Reserva reservaEncolada = reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino, true);

		when(alquiler.hayReservasEncoladas()).thenReturn(true);

		when(alquiler.obtenerPrimeroDeReservasEncoladas()).thenReturn(reservaEncolada);

		reservaManager.cancelarReserva(reservaActiva, inquilino, mock(NotificadorManager.class), mailSender);

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
			reservaManager.cancelarReserva(reservaMock, inquilino, mock(NotificadorManager.class), mailSender);
		});
	}

	@Test
	public void testGetReservasFiltraCanceladasYFinalizadas() throws Exception {
		Inmueble inmuebleMock = mock(Inmueble.class);
		when(alquiler.getInmueble()).thenReturn(inmuebleMock);
		when(inmuebleMock.getPropietario()).thenReturn(propietario);
		when(alquiler.existeFormaDePago(formaDePago)).thenReturn(true);

		when(alquiler.puedeCrearReserva(entrada, salida)).thenReturn(true);
		when(alquiler.puedeCrearReserva(entrada.plusDays(10), salida.plusDays(10))).thenReturn(true);
		when(alquiler.puedeCrearReserva(entrada.plusDays(20), salida.plusDays(20))).thenReturn(true);

		Reserva reserva1 = reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino, false);
		Reserva reserva2 = reservaManager.crearReserva(formaDePago, entrada.plusDays(10), salida.plusDays(10), alquiler,
				inquilino, false);
		Reserva reserva3 = reservaManager.crearReserva(formaDePago, entrada.plusDays(20), salida.plusDays(20), alquiler,
				inquilino, false);

		assertTrue(reservaManager.getReservas().contains(reserva2),
				"La reserva 2 debería estar en la lista antes de cancelar.");
		reservaManager.aceptarReserva(reserva2, propietario, mock(NotificadorManager.class), mailSender);
		reservaManager.aceptarReserva(reserva3, propietario, mock(NotificadorManager.class), mailSender);
		reservaManager.aceptarReserva(reserva1, propietario, mock(NotificadorManager.class), mailSender);

		reservaManager.cancelarReserva(reserva2, inquilino, mock(NotificadorManager.class), mailSender);

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

	@Test
	public void testSeFiltranReservas() throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException {
		FiltroReserva fr = mock(FiltroReservasFuturas.class);
		when(alquiler.existeFormaDePago(formaDePago)).thenReturn(true);
		when(alquiler.puedeCrearReserva(entrada, salida)).thenReturn(true);
		Reserva re = this.reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino, false);

		this.reservaManager.filtrarReservas(fr);
		verify(fr, times(1)).filtrarReservas(Arrays.asList(re));
	}
	 @Test
	    public void testCrearReservaThrowsAlquilerNoDisponibleException() {
	       
	        FormaDePago formaDePago = FormaDePago.CREDITO;
	        LocalDate entrada = LocalDate.of(2023, 5, 1);
	        LocalDate salida = LocalDate.of(2023, 5, 10);
	        boolean esReservaCondicional = false;
	        Alquiler alquilerMock = mock(Alquiler.class);
	        when(alquilerMock.existeFormaDePago(formaDePago)).thenReturn(true);
	        when(alquilerMock.puedeCrearReserva(entrada, salida)).thenReturn(false);

	        assertThrows(AlquilerNoDisponibleException.class, () -> {
	            reservaManager.crearReserva(formaDePago, entrada, salida, alquilerMock, inquilino, esReservaCondicional);
	        });
	    
	}
}
