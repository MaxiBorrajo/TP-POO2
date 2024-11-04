package sistema.notificador;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.notificaciones.CancelacionNotify;
import sistema.notificaciones.EventoNotificador;
import sistema.notificaciones.Listener;
import sistema.notificaciones.Suscriptor;

public class ListenerTest {
	private Listener lis;
	private EventoNotificador even;
	
	@BeforeEach
	void setUp() {
		this.even = mock(CancelacionNotify.class);
		this.lis = new Listener(even);
	}
	
	@Test
	void tesSepuedeAgregarSuscriptores() {
		assertEquals(0, this.lis.cantidadSuscriptos());
		Suscriptor s = mock(Suscriptor.class);
		this.lis.add(s);
		assertEquals(1, this.lis.cantidadSuscriptos());
		
	}
	
	@Test
	void tesSepuedeRemoverSuscriptores() {
		assertEquals(0, this.lis.cantidadSuscriptos());
		Suscriptor s = mock(Suscriptor.class);
		this.lis.add(s);
		assertEquals(1, this.lis.cantidadSuscriptos());
		this.lis.remove(s);
		assertEquals(0, this.lis.cantidadSuscriptos());
		
	}
	@Test
	void testPuedeSberSIElEventoEsComoOtro() {
		EventoNotificador ev = mock(CancelacionNotify.class); 
		
		this.lis.esEventoPara(ev);
		verify(this.even, times(1)).esIgualA(ev);
	}
	@Test
	void testPuedeNotificarATodosSuscriptos() {
		Suscriptor s = mock(Suscriptor.class);
		this.lis.add(s);
		Suscriptor s1 = mock(Suscriptor.class);
		this.lis.add(s1);
		
		this.lis.notificar();
		
		verify(this.even, times(1)).notificarEspecifica(s);
		verify(this.even, times(1)).notificarEspecifica(s1);
	}
	
	
}
