package sistema.notificador;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.alquiler.Alquiler;
import sistema.notificaciones.BajaPrecioNotify;
import sistema.notificaciones.CancelacionNotify;
import sistema.notificaciones.EventoNotificador;
import sistema.notificaciones.ReservaNotify;
import sistema.notificaciones.Suscriptor;

public class ReservaNotifyTest {
	private ReservaNotify can;
	private Alquiler alq;
	@BeforeEach
	void setUp() {
		this.alq = mock(Alquiler.class);
		this.can = new ReservaNotify(alq);
	}
	
	@Test
	void testSabeREsponderSiEsCancelado() {
		assertTrue(this.can.esReserva());
	
	}
	@Test
	void testSabeResponderSiSonElMimsoEvento() {
		EventoNotificador even = mock(ReservaNotify.class);
		when(even.esReserva()).thenReturn(true);
		assertTrue(this.can.esElMismoEvento(even));
	}
	
	@Test
	void testSabeResponderSiNoSonElMimsoEvento() {
		EventoNotificador even = mock(BajaPrecioNotify.class);
		when(even.esReserva()).thenReturn(false);
		
		assertFalse(this.can.esElMismoEvento(even));
	}
	@Test
	void testMandaUnUpdateAlSuscriptorAlSerNotificada() {
		Suscriptor sus = mock(Suscriptor.class);
		
		this.can.notificarEspecifica(sus);
		
		verify(sus,times(1)).update(this.can);
	}

}
