package sistema.alquiler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;
import sistema.alquiler.politicaDeCancelacion.PoliticaDeCancelacion;
import sistema.enums.FormaDePago;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.YaExistenteException;
import sistema.managers.NotificadorManager;
import sistema.managers.ReservaManager;
import sistema.notificaciones.BajaPrecioNotify;
import sistema.periodo.Periodo;
import sistema.reserva.Reserva;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AlquilerTest {

	private Alquiler alquiler;
	private Inmueble inmuebleMock;
	private PoliticaDeCancelacion politicaDeCancelacionMock;
	private List<FormaDePago> formasDePago;

	@BeforeEach
	public void setUp() {
		inmuebleMock = mock(Inmueble.class);
		politicaDeCancelacionMock = mock(PoliticaDeCancelacion.class);
		formasDePago = new ArrayList<>();
		formasDePago.add(FormaDePago.CREDITO);
		formasDePago.add(FormaDePago.EFECTIVO);

		alquiler = new Alquiler(inmuebleMock, LocalTime.of(14, 0), LocalTime.of(11, 0), 100.0,
				politicaDeCancelacionMock, formasDePago);
	}
	
	

	@Test
	public void testAgregarPeriodoSinConflictos() {
		LocalDate inicio = LocalDate.of(2023, 1, 1);
		LocalDate fin = LocalDate.of(2023, 1, 10);
		Periodo periodo = new Periodo(inicio, fin, 150.0);

		assertDoesNotThrow(() -> alquiler.agregarPeriodo(periodo));

	}
	
	@Test
	public void testDevuelveSuAlquiler() {
		assertEquals(this.alquiler, this.alquiler.getAlquiler());
	}

	@Test
	public void testAgregarPeriodoConflictoConPeriodoExistente() {
		Periodo periodo1 = new Periodo(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 10), 150.0);
		Periodo periodo2 = new Periodo(LocalDate.of(2023, 1, 5), LocalDate.of(2023, 1, 15), 200.0);
		alquiler.agregarPeriodo(periodo1);

		assertThrows(IllegalArgumentException.class, () -> alquiler.agregarPeriodo(periodo2));
	}

	@Test
	public void testAgregarFormaDePagoFormaYaExistente() {
		assertThrows(YaExistenteException.class, () -> alquiler.agregarFormaDePago(FormaDePago.CREDITO));
	}

	@Test
	public void testAgregarFormaDePagoNueva() throws YaExistenteException {
		alquiler.agregarFormaDePago(FormaDePago.DEBITO);
		assertTrue(alquiler.existeFormaDePago(FormaDePago.DEBITO));
	}

	@Test
	public void testEncolarReservaYHayReservasEncoladas() {
		Reserva reservaMock = mock(Reserva.class);
		alquiler.encolarReserva(reservaMock);

		assertTrue(alquiler.hayReservasEncoladas());
	}

	@Test
	public void testSeCanceloReservaSinReservasEncoladas()
			throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException {
		Reserva reservaMock = mock(Reserva.class);
		ReservaManager reservaManagerMock = mock(ReservaManager.class);
		when(reservaMock.getFechaInicio()).thenReturn(LocalDate.of(2023, 5, 1));
		when(reservaMock.getFechaFinal()).thenReturn(LocalDate.of(2023, 5, 3));

		alquiler.seCanceloReserva(reservaMock, reservaManagerMock);

		verify(reservaManagerMock, never()).desencolarReserva(alquiler);
	}

	@Test
	public void testSeCanceloReservaConReservasEncoladas()
			throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException {
		Reserva reservaMock = mock(Reserva.class);
		Reserva reservaEncoladaMock = mock(Reserva.class);
		ReservaManager reservaManagerMock = mock(ReservaManager.class);
		alquiler.encolarReserva(reservaEncoladaMock);

		when(reservaMock.getFechaInicio()).thenReturn(LocalDate.of(2023, 5, 1));
		when(reservaMock.getFechaFinal()).thenReturn(LocalDate.of(2023, 5, 3));

		alquiler.seCanceloReserva(reservaMock, reservaManagerMock);

		verify(reservaManagerMock, times(1)).desencolarReserva(alquiler);
	}

	@Test
	public void testPuedeCrearReservaDiasDisponibles() {
		LocalDate entrada = LocalDate.of(2023, 5, 1);
		LocalDate salida = LocalDate.of(2023, 5, 3);

		assertTrue(alquiler.puedeCrearReserva(entrada, salida));
	}

	@Test
	public void testPuedeCrearReservaConDiasNoDisponibles() {
		Reserva reservaMock = mock(Reserva.class);
		LocalDate fechaInicio = LocalDate.of(2023, 6, 1);
		LocalDate fechaFinal = LocalDate.of(2023, 6, 3);

		when(reservaMock.getFechaInicio()).thenReturn(fechaInicio);
		when(reservaMock.getFechaFinal()).thenReturn(fechaFinal);

		alquiler.seAceptoReserva(reservaMock);

		assertFalse(alquiler.puedeCrearReserva(LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 3)));
	}

	@Test
	public void testSeAceptoReserva() {
		Reserva reservaMock = mock(Reserva.class);
		LocalDate fechaInicio = LocalDate.of(2023, 6, 1);
		LocalDate fechaFinal = LocalDate.of(2023, 6, 3);

		when(reservaMock.getFechaInicio()).thenReturn(fechaInicio);
		when(reservaMock.getFechaFinal()).thenReturn(fechaFinal);

		alquiler.seAceptoReserva(reservaMock);

		verify(inmuebleMock, times(1)).setVecesAlquilado(anyInt());
	}

	@Test
	public void testCalcularPrecioPeriodoSoloPrecioDefault() {
		LocalDate inicio = LocalDate.of(2023, 2, 1);
		LocalDate fin = LocalDate.of(2023, 2, 3);

		double precio = alquiler.calcularPrecioPeriodo(inicio, fin);
		assertEquals(300.0, precio);
	}

	@Test
	public void testCalcularPrecioPeriodoConPeriodosEspeciales() {
		Periodo periodo = new Periodo(LocalDate.of(2023, 2, 1), LocalDate.of(2023, 2, 2), 200.0);
		alquiler.agregarPeriodo(periodo);

		double precio = alquiler.calcularPrecioPeriodo(LocalDate.of(2023, 2, 1), LocalDate.of(2023, 2, 3));
		assertEquals(500.0, precio);
	}

	@Test
	public void testCalcularReembolsoPorCancelacion() {
		LocalDate inicio = LocalDate.of(2023, 5, 1);
		LocalDate fin = LocalDate.of(2023, 5, 3);
		double precioTotal = 300.0;

		when(politicaDeCancelacionMock.calcularReembolso(inicio, fin, precioTotal)).thenReturn(150.0);

		double reembolso = alquiler.calcularReembolsoPorCancelacion(inicio, fin, precioTotal);
		assertEquals(150.0, reembolso);
	}

	@Test
	public void testGetInmueble() {
		assertEquals(inmuebleMock, alquiler.getInmueble());
	}

	@Test
	public void testAceptaCantidadHuespedes() {
		when(inmuebleMock.getCapacidad()).thenReturn(4);

		assertTrue(alquiler.aceptaCantidadHuespedes(5));
		assertFalse(alquiler.aceptaCantidadHuespedes(3));
	}

	@Test
	public void testSetearNuevaPoliticaDeCancelacion() {
		PoliticaDeCancelacion nuevaPolitica = mock(PoliticaDeCancelacion.class);
		alquiler.setPoliticaDeCancelacion(nuevaPolitica);
		assertEquals(nuevaPolitica, alquiler.getPoliticaDeCancelacion());
	}

	

	@Test
	public void testGetTipoDeInmueble() {
		String expectedTipo = "Casa";
		when(inmuebleMock.getTipo()).thenReturn(expectedTipo);

		String tipoDeInmueble = alquiler.getTipoDeInmueble();

		assertEquals(expectedTipo, tipoDeInmueble);
	}

	@Test
	public void testTienenElMismoInmueble() {
		Alquiler otherAlquiler = new Alquiler(inmuebleMock, LocalTime.of(14, 0), LocalTime.of(11, 0), 100.0,
				mock(PoliticaDeCancelacion.class), List.of());

		when(inmuebleMock.sonElMismoInmueble(inmuebleMock)).thenReturn(true);

		boolean result = alquiler.tienenElMismoInmueble(otherAlquiler);

		assertTrue(result);
	}
    @Test
    public void testGetCiudad() {
        String expectedCiudad = "Buenos Aires";
        when(inmuebleMock.getCiudad()).thenReturn(expectedCiudad);
        String ciudad = alquiler.getCiudad();
        when(inmuebleMock.esDeCiudad(ciudad)).thenReturn(true);

        assertEquals(expectedCiudad, ciudad);
        assertTrue(alquiler.esDeCiudad(ciudad));
    }
    

    @Test
    public void testObtenerPrimeroDeReservasEncoladas() {
        // Arrange
        Reserva reservaMock = mock(Reserva.class);
        alquiler = new Alquiler(
                inmuebleMock,
                LocalTime.of(14, 0),
                LocalTime.of(11, 0),
                100.0,
                mock(PoliticaDeCancelacion.class),
                List.of()
        );
        alquiler.encolarReserva(reservaMock);
        Reserva reserva = alquiler.obtenerPrimeroDeReservasEncoladas();

        assertNotNull(reserva);
        assertEquals(reservaMock, reserva);
    }

    @Test
    public void testObtenerPrimeroDeReservasEncoladasWhenQueueIsEmpty() {
        Reserva reserva = alquiler.obtenerPrimeroDeReservasEncoladas();
        assertNull(reserva);
    }
    @Test
    public void testNoCumplePrecioEnPeriodoConPrecioMenorAlMinimo() {
        LocalDate entrada = LocalDate.of(2024, 11, 10);
        LocalDate salida = LocalDate.of(2024, 11, 15);
        double precioMinimo = 900.0;
        double precioMaximo = 1000.0;
        
        assertFalse(alquiler.cumplePrecioEnPeriodo(precioMinimo, precioMaximo, entrada, salida));
    }

    @Test
    public void testNoCumplePrecioEnPeriodoConPrecioMayorAlMaximo() {
        LocalDate entrada = LocalDate.of(2024, 11, 10);
        LocalDate salida = LocalDate.of(2024, 11, 20);
        double precioMinimo = 1000.0;
        double precioMaximo = 1200.0;
        
        assertFalse(alquiler.cumplePrecioEnPeriodo(precioMinimo, precioMaximo, entrada, salida));
    }
    
    @Test
    public void testBajaDePrecioTiraNotificacion() {
    	NotificadorManager noti = mock(NotificadorManager.class);
    	this.alquiler.cambiarPrecio(88d, noti);
    	
    	verify(noti).notify(any(BajaPrecioNotify.class));
    	assertEquals(88d, this.alquiler.getPrecioBase());
    }
}
