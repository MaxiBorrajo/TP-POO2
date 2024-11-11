package sistema.notificador;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.alquiler.Alquiler;
import sistema.notificaciones.CancelacionNotify;
import sistema.notificaciones.EventoNotificador;
import sistema.notificaciones.ReservaNotify;
import sistema.reserva.Reserva;

public class EventoNotificadorTest {
	private EventoNotificador even;
	private EventoNotificador even2;
	private Reserva re;

	@BeforeEach
	void setUp() {
		this.re = mock(Reserva.class);
		this.even = new CancelacionNotify(re);
		this.even2 = new ReservaNotify(re);
	}

	@Test
	void testSabenQueEventoNotificadorNoSon() {
		assertFalse(this.even.esBajaDePrecio());
		assertFalse(this.even.esReserva());
		assertFalse(this.even2.esCancelacion());
		assertFalse(this.even2.esBajaDePrecio());
	}

	@Test
	void testSabeSiEsElMismoObservable() {

		assertTrue(even2.esElMismoObservable(this.re));

	}

	@Test
	void testSabeSiNoEsElMismoEventoPeroMismoObservable() {

		assertFalse(this.even.esIgualA(this.even2));

	}

	@Test
	void testSabeSiEsElMismoEventoMismoInmueble() {

		EventoNotificador even1 = new CancelacionNotify(this.re);

		assertTrue(this.even.esIgualA(even1));

	}

	@Test
	void testSabeSiEsElMismoEventoDistintoObservable() {

		Reserva r1 = mock(Reserva.class);
		EventoNotificador even1 = new CancelacionNotify(r1);

		assertFalse(this.even.esIgualA(even1));

	}

}
