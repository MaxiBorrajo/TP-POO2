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

public class EventoNotificadorTest {
	private EventoNotificador even;
	private EventoNotificador even2;
	private Alquiler alq;
	
	@BeforeEach
	void setUp() {
		this.alq = mock(Alquiler.class);
		this.even = new CancelacionNotify(alq);
		this.even2 = new ReservaNotify(alq);
	}
	
	
	@Test
	void testSabenQueEventoNotificadorNoSon() {
		assertFalse(this.even.esBajaDePrecio());
		assertFalse(this.even.esReserva());
		assertFalse(this.even2.esCancelacion());
		assertFalse(this.even2.esBajaDePrecio());
	}
	
	
	@Test
	void testSabeSiEsElMismoAlquiler() {
		
		when(this.alq.tienenElMismoInmueble(alq)).thenReturn(true);
		
		assertTrue(even2.esElMismoAlquiler(this.alq));
	
	}
	
	@Test
	void testSabeSiNoEsElMismoEventoPeroMismoInmueble() {
		when(this.alq.tienenElMismoInmueble(alq)).thenReturn(true);
		
		
		assertFalse(this.even.esIgualA(this.even2));
		
		
		
	}
	
	@Test
	void testSabeSiEsElMismoEventoMismoInmueble() {
		
		Alquiler a1 = mock(Alquiler.class);
		EventoNotificador even1 =  new CancelacionNotify(a1);
		
		when(a1.tienenElMismoInmueble(this.alq)).thenReturn(true);
		
		assertTrue(this.even.esIgualA(even1));
		
		
		
	}
	
	@Test
	void testSabeSiEsElMismoEventoDistintoInmueble() {
		
		Alquiler a1 = mock(Alquiler.class);
		EventoNotificador even1 =  new CancelacionNotify(a1);
		
		when(this.alq.tienenElMismoInmueble(a1)).thenReturn(false);
		
		assertFalse(this.even.esIgualA(even1));
		
		
		
	}
	
}
