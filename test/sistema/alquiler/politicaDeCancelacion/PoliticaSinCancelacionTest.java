package sistema.alquiler.politicaDeCancelacion;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PoliticaSinCancelacionTest {

	private PoliticaDeCancelacion sinCancelacion;

	@BeforeEach
	public void setUp() {
		sinCancelacion = new SinCancelacion();
	} 

	@Test
	public void testSinCancelacionDevuelveCeroSiempre() {
		LocalDate fechaInicio = LocalDate.now().plusDays(15);
		LocalDate fechaFinal = fechaInicio.plusDays(5);
		double precioTotal = 1000;

		double reembolso = sinCancelacion.calcularReembolso(fechaInicio, fechaFinal, precioTotal);
		assertEquals(0.0, reembolso, "Expected no refund");

	}
}