package sistema.Inmueble;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UbicacionTest {
	private Ubicacion ubi;
	@BeforeEach
	void setUp() {
		this.ubi = new Ubicacion("Argentina","Quilmes","x21345");
	}
	
	@Test
	void testSabeEnCompararCiudades() {
		assertTrue(this.ubi.estaEnCiudad("Quilmes"));
		assertFalse(this.ubi.estaEnCiudad("Bernal"));
	}
	
	@Test
	void testSabeDecirSuCiudad() {
		assertEquals(this.ubi.getCiudad(),"Quilmes");
	}
	 
}
