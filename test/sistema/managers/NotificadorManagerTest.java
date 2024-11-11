package sistema.managers;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.exceptions.NoSuscriptoAlEvento;
import sistema.notificaciones.*;
import sistema.notificaciones.Suscriptor;

public class NotificadorManagerTest {

	private NotificadorManager noti;

	@BeforeEach
	void setUp() {
		this.noti = new NotificadorManager();
	}

	@Test
	public void testSeAgregaSuscriptores() throws NoSuscriptoAlEvento {
		assertEquals(0, this.noti.cantidadSuscriptores());
		Suscriptor s = mock(Suscriptor.class);
		EventoNotificador even = mock(CancelacionNotify.class);
		when(even.esIgualA(even)).thenReturn(true);

		this.noti.addSuscriptor(s, even);

		assertEquals(1, this.noti.cantidadSuscriptores());
		assertTrue(this.noti.estaRegistrado(s, even));

	}

	@Test
	public void testSeAgreganVariosSuscriptoresAlMismoEvento() throws NoSuscriptoAlEvento {
		assertEquals(0, this.noti.cantidadSuscriptores());
		Suscriptor s = mock(Suscriptor.class);
		Suscriptor s1 = mock(Suscriptor.class);
		EventoNotificador even = mock(CancelacionNotify.class);
		EventoNotificador even2 = mock(CancelacionNotify.class);
		when(even.esIgualA(even)).thenReturn(true);
		when(even.esIgualA(even2)).thenReturn(true);

		this.noti.addSuscriptor(s, even);
		this.noti.addSuscriptor(s1, even2);

		assertEquals(2, this.noti.cantidadSuscriptores());
		assertTrue(this.noti.estaRegistrado(s, even));
		assertTrue(this.noti.estaRegistrado(s1, even));

	}

	@Test
	public void testSeAgreganVariosSuscriptoresADistintoEvento() throws NoSuscriptoAlEvento {
		assertEquals(0, this.noti.cantidadSuscriptores());
		Suscriptor s = mock(Suscriptor.class);
		Suscriptor s1 = mock(Suscriptor.class);
		EventoNotificador even = mock(CancelacionNotify.class);
		EventoNotificador even2 = mock(ReservaNotify.class);
		when(even.esIgualA(even2)).thenReturn(false);
		when(even.esIgualA(even)).thenReturn(true);
		when(even2.esIgualA(even2)).thenReturn(true);
		this.noti.addSuscriptor(s, even);
		this.noti.addSuscriptor(s1, even2);

		assertEquals(2, this.noti.cantidadSuscriptores());

		assertTrue(this.noti.estaRegistrado(s, even));
		assertTrue(this.noti.estaRegistrado(s1, even2));
	}

	@Test
	public void testSeRemueveUnSuscripor() throws NoSuscriptoAlEvento {

		Suscriptor s = mock(Suscriptor.class);
		EventoNotificador even = mock(CancelacionNotify.class);
		when(even.esIgualA(even)).thenReturn(true);

		this.noti.addSuscriptor(s, even);

		assertEquals(1, this.noti.cantidadSuscriptores());

		this.noti.removeSuscriptor(s, even);

		assertEquals(0, this.noti.cantidadSuscriptores());
	}

	@Test
	public void testRemueveUnSuscriporThrowExceptionSiNoEstaElEvento() throws NoSuscriptoAlEvento {

		Suscriptor s = mock(Suscriptor.class);
		EventoNotificador even = mock(CancelacionNotify.class);

		assertThrows(NoSuscriptoAlEvento.class, (() -> this.noti.removeSuscriptor(s, even)));

	}

	@Test
	public void testNoSePuedeSaberSiUnSuscriporEstaEnUnEventoQueNoExisteThrowExceptionSiNoEstaElEvento()
			throws NoSuscriptoAlEvento {

		Suscriptor s = mock(Suscriptor.class);
		EventoNotificador even = mock(CancelacionNotify.class);

		assertThrows(NoSuscriptoAlEvento.class, (() -> this.noti.estaRegistrado(s, even)));

	}

	@Test

	public void testPuedeNotificarATodosSusListener() {
		EventoNotificador even = mock(CancelacionNotify.class);
		EventoNotificador even2 = mock(CancelacionNotify.class);
		Suscriptor s = mock(Suscriptor.class);
		Suscriptor s1 = mock(Suscriptor.class);
		Suscriptor s2 = mock(Suscriptor.class);
		when(even.esIgualA(even2)).thenReturn(false);
		when(even.esIgualA(even)).thenReturn(true);
		this.noti.addSuscriptor(s, even);
		this.noti.addSuscriptor(s1, even);
		this.noti.addSuscriptor(s2, even2);
		this.noti.notify(even);

		verify(even, times(1)).notificarEspecifica(s);
		verify(even, times(1)).notificarEspecifica(s1);
		verify(even, times(0)).notificarEspecifica(s2);

	}
}
