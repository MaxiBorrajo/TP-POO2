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
import sistema.notificaciones.Suscriptor;

public class BajaPrecioNotifyTest {
	private BajaPrecioNotify can;
	private Alquiler alq;
	@BeforeEach
	void setUp() {
		this.alq = mock(Alquiler.class);
		this.can = new BajaPrecioNotify(alq);
	}
	
	@Test
	void testSabeREsponderSiEsCancelado() {
		assertTrue(this.can.esBajaDePrecio());
	
	}
	@Test
	void testSabeResponderSiSonElMimsoEvento() {
		EventoNotificador even = mock(BajaPrecioNotify.class);
		when(even.esBajaDePrecio()).thenReturn(true);
		assertTrue(this.can.esElMismoEvento(even));
	}
	
	@Test
	void testSabeResponderSiNoSonElMimsoEvento() {
		EventoNotificador even = mock(CancelacionNotify.class);
		when(even.esBajaDePrecio()).thenReturn(false);
		
		assertFalse(this.can.esElMismoEvento(even));
	}
	@Test
	void testMandaUnUpdateAlSuscriptorAlSerNotificada() {
		Suscriptor sus = mock(Suscriptor.class);
		when(this.alq.getTipoDeInmueble()).thenReturn("Inmueble");
		when(this.alq.getPrecioBase()).thenReturn(200d);
		this.can.notificarEspecifica(sus);
		
		verify(sus,times(1)).publish("No te pierdas esta oferta: Un inmueble "+ "Inmueble" + " a tan solo " + 200d + " pesos ");
	}
}
