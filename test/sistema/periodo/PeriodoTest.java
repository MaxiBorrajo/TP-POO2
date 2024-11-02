package sistema.periodo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PeriodoTest {
	private Periodo p;
	
	@BeforeEach
	void setUp() {
		this.p = new Periodo(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 2, 1), 2000d);
	}
	
	@Test
	void unPeriodoSabeSuPrecio() {
		assertEquals(2000d, this.p.getPrecio());
	}
	
	
	
}
