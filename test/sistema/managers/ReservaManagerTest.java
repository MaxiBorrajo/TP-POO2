package sistema.managers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;


import sistema.alquiler.Alquiler;
import sistema.enums.FormaDePago;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.reserva.Reserva;
import sistema.usuario.*;

public class ReservaManagerTest {
	private ReservaManager reservaManager;
	private Inquilino inquilino;
	private Alquiler alquiler;
	private FormaDePago formaDePago;
	private LocalDate entrada, salida;

	@Before
	public void setUp() throws Exception {
		this.reservaManager = new ReservaManager();
		this.inquilino = mock(Inquilino.class);
		this.alquiler = mock(Alquiler.class);

		this.formaDePago = mock(FormaDePago.class);
		this.entrada = mock(LocalDate.class);
		this.salida = mock(LocalDate.class);


	}

	@Test
	public void unInquilinoPuedeCrearUnaReservaValida()
			throws Exception {
		

		when(alquiler.validateFormaDePago(formaDePago)).thenReturn(true);
		when(alquiler.puedeCrearReserva(entrada, salida)).thenReturn(true);

		Reserva reserva = reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino);

		assertNotNull(reserva);
	}

	@Test
	public void unInquilinooNoPuedeCrearUnaReservaSiNoEstaDisponible() {


		when(alquiler.validateFormaDePago(formaDePago)).thenReturn(true);
		when(alquiler.puedeCrearReserva(entrada, salida)).thenReturn(false);

		assertThrows(AlquilerNoDisponibleException.class, () -> {
			reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino);
		});

	}

	@Test
	public void UnInquilinoNoPuedeCrearReservaConFormaDePagoNoAceptada() {

		when(alquiler.validateFormaDePago(formaDePago)).thenReturn(false);
		when(alquiler.puedeCrearReserva(entrada, salida)).thenReturn(true);

		assertThrows(FormaDePagoNoAceptadaException.class, () -> {
			reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, inquilino);
		});
	}

}
