package sistema.alquiler.politicaDeCancelacion;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PoliticaDeCancelacionIntermediaTest {

	private PoliticaDeCancelacion intermedia;

	@BeforeEach 
	public void setUp() {
		intermedia = new CancelacionIntermedia();
	}

	@Test
	public void testCancelacionIntermediaDevuelveTodoElDinero() {

		LocalDate fechaInicio = LocalDate.now().plusDays(25);
		LocalDate fechaFinal = fechaInicio.plusDays(5);
		double precioTotal = 1000;

		double reembolso = intermedia.calcularReembolso(fechaInicio, fechaFinal, precioTotal);
		assertEquals(precioTotal, reembolso, "Expected full refund");
	}

	@Test
	public void testCancelacionIntermediaDevuelveMitadDelDinero() {
		LocalDate fechaInicio = LocalDate.now().plusDays(15);
		LocalDate fechaFinal = fechaInicio.plusDays(5);
		double precioTotal = 1000;

		double reembolso = intermedia.calcularReembolso(fechaInicio, fechaFinal, precioTotal);
		assertEquals(precioTotal * 0.5, reembolso, "Expected 50% refund");
	}

	@Test
	public void testCancelacionIntermediaSinReembolso() {
		LocalDate fechaInicio = LocalDate.now().plusDays(5);
		LocalDate fechaFinal = fechaInicio.plusDays(5);
		double precioTotal = 1000;

		double reembolso = intermedia.calcularReembolso(fechaInicio, fechaFinal, precioTotal);
		assertEquals(0.0, reembolso, "Expected no refund");
	}
}
