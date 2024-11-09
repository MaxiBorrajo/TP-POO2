package sistema.filtro;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

public class FiltroReservaTest {
	private List<Reserva> xs;
	private Reserva res1;
	private Reserva res2;
	@BeforeEach
	void setUp() {
		this.res1 = mock(Reserva.class);
		this.res2 = mock(Reserva.class);
		this.xs = Arrays.asList(this.res1, this.res2);
	}
	
	
	@Test
	void testSeFiltranPorTodosLosReservaDelUsuario() {
			Usuario us = mock(Usuario.class);
			when(this.res1.esDeUsuario(us)).thenReturn(true);
			when(this.res2.esDeUsuario(us)).thenReturn(false);
			FiltroReserva res = new FiltroTodasReservas(us);
			
			assertEquals(1,res.filtrarReservas(this.xs).size());
	
	}
	
	@Test
	void testSeFiltranPorReservaDeCiudad() {
			Usuario us = mock(Usuario.class);
			when(this.res1.esDeUsuario(us)).thenReturn(true);
			when(this.res2.esDeUsuario(us)).thenReturn(false);
			FiltroReserva res = new FiltroReservaCiudad(us,"Quilmes");
			when(this.res1.esDeCiudad("Quilmes")).thenReturn(true);
			when(this.res2.esDeCiudad("Quilmes")).thenReturn(false);
			assertEquals(1,res.filtrarReservas(this.xs).size());
	}
	
	@Test
	void testSeFiltranPorReservaPosteriores() {
			Usuario us = mock(Usuario.class);
			when(this.res1.esDeUsuario(us)).thenReturn(true);
			when(this.res2.esDeUsuario(us)).thenReturn(false);
			FiltroReserva res = new FiltroReservasFuturas(us);
			when(this.res1.fechaPosteriorAInicio(LocalDate.now())).thenReturn(true);
			when(this.res2.fechaPosteriorAInicio(LocalDate.now())).thenReturn(false);
			
			assertEquals(1,res.filtrarReservas(this.xs).size());
	}
	
	
	
	
	
}
