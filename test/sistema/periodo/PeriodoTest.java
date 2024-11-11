package sistema.periodo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PeriodoTest {
	private Periodo periodo;

	@BeforeEach
	void setUp() {
		this.periodo = new Periodo(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 2, 1), 2000d);
	}

	@Test
	void unPeriodoSabeSuPrecio() {
		assertEquals(2000d, this.periodo.getPrecio());
	}

	@Test
	void unPeriodoSabeSiUnaFechaPerteneceAlPeriodo() {
		assertTrue(periodo.perteneceAPeriodo(LocalDate.of(2024, 1, 15)));
		// Fecha inicial
		assertTrue(periodo.perteneceAPeriodo(LocalDate.of(2024, 1, 1)));
		// Fecha final
		assertTrue(periodo.perteneceAPeriodo(LocalDate.of(2024, 2, 1)));

		assertFalse(periodo.perteneceAPeriodo(LocalDate.of(2023, 12, 31)));
		assertFalse(periodo.perteneceAPeriodo(LocalDate.of(2024, 2, 2)));
	}

	@Test
	void unPeriodoComparaConPrecioMinimo() {
		assertFalse(periodo.precioPeriodoEsMenorA(1500d));
		assertTrue(periodo.precioPeriodoEsMenorA(2000d));
		assertTrue(periodo.precioPeriodoEsMenorA(2500d));
	}

	@Test
	void unPeriodoComparaConPrecioMaximo() {
		assertFalse(periodo.precioPeriodoMayorA(2500d));
		assertTrue(periodo.precioPeriodoMayorA(2000d));
		assertTrue(periodo.precioPeriodoMayorA(1500d));
	}

	@Test
	void unPeriodoSabeSiTieneLasMismasFechas() {
		Periodo mismoPeriodo = new Periodo(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 2, 1), 1500d);
		Periodo periodoDiferente = new Periodo(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1), 1500d);

		assertTrue(periodo.periodoDeFecha(mismoPeriodo.getFechaInicio(), mismoPeriodo.getFechaFinal()));
		assertFalse(periodo.periodoDeFecha(periodoDiferente.getFechaInicio(), periodoDiferente.getFechaFinal()));
	}
}
