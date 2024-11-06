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

public class CancelacionNotifyTest {
	private CancelacionNotify can;
	private Alquiler alq;
	@BeforeEach
	void setUp() {
		this.alq = mock(Alquiler.class);
		this.can = new CancelacionNotify(alq);
	}
	
	@Test
	void testSabeREsponderSiEsCancelado() {
		assertTrue(this.can.esCancelacion());
	
	}
	@Test
	void testSabeResponderSiSonElMimsoEvento() {
		EventoNotificador even = mock(CancelacionNotify.class);
		when(even.esCancelacion()).thenReturn(true);
		assertTrue(this.can.esElMismoEvento(even));
	}
	
	@Test
	void testSabeResponderSiNoSonElMimsoEvento() {
		EventoNotificador even = mock(BajaPrecioNotify.class);
		when(even.esCancelacion()).thenReturn(false);
		
		assertFalse(this.can.esElMismoEvento(even));
	}
	@Test
	void testMandaUnPublishAlSuscriptorAlSerNotificada() {
		Suscriptor sus = mock(Suscriptor.class);
		when(this.alq.getTipoDeInmueble()).thenReturn("Inmueble");
		this.can.notificarEspecifica(sus);
		
		verify(sus,times(1)).popUp("El/la " +"Inmueble" + "que te interesa se ha liberado! Corre a reservarlo!","ROJO",24);
	}

}