package sistema.alquiler.politicaDeCancelacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;
import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;
import sistema.enums.FormaDePago;

public class PoliticaDeCancelacionGratuitaTest {

	private PoliticaDeCancelacion gratuita;

	@BeforeEach
	public void setUp() {
		gratuita = new CancelacionGratuita();
	}

	@Test
	public void testCancelacionGratuitaDevuelveTodoElDinero() {
		LocalDate fechaInicio = LocalDate.now().plusDays(15);
		LocalDate fechaFinal = fechaInicio.plusDays(5);
		double precioTotal = 1000;

		double reembolso = gratuita.calcularReembolso(fechaInicio, fechaFinal, precioTotal);
		assertEquals(precioTotal, reembolso, "Expected full refund");
	}

	@Test
	public void testCancelacionGratuitaDevuelveReembolsoParcial() {
		LocalDate fechaInicio = LocalDate.now().plusDays(5);
		LocalDate fechaFinal = fechaInicio.plusDays(5);
		double precioTotal = 1000;

		double reembolsoEsperado = precioTotal - (2 * (precioTotal / 5));
		double reembolso = gratuita.calcularReembolso(fechaInicio, fechaFinal, precioTotal);

		assertEquals(reembolsoEsperado, reembolso, "Expected partial refund");
	}

	@Test
	public void testCancelacionGratuitaSinReembolso() {
		LocalDate fechaInicio = LocalDate.now().minusDays(1);
		LocalDate fechaFinal = fechaInicio.plusDays(5);
		double precioTotal = 1000;

		double reembolso = gratuita.calcularReembolso(fechaInicio, fechaFinal, precioTotal);
		assertEquals(0, reembolso, "Expected no refund");
	}

}
