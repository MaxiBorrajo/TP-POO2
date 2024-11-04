package sistema.usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.Inmueble.Inmueble;
import sistema.exceptions.InmuebleYaGuardadoException;


public class PropietarioTest {
	private Propietario propietario;
	private Inmueble inmueble;

	@BeforeEach
	void setUp(){
		this.propietario = new Propietario("ginebra", "ginebra@gmail.com", "+5401166666666");
		this.inmueble = mock(Inmueble.class);
	}

	@Test
	public void testPropietarioPuedeAsociarUnInmueble() throws InmuebleYaGuardadoException {
		assertEquals(0, propietario.getInmuebles().size(), "Initial Inmueble count should be 0");

		propietario.addInmueble(this.inmueble);

		assertEquals(1, propietario.getInmuebles().size(), "Inmueble count should be 1");
		assertTrue(propietario.tieneInmueble(this.inmueble));
	}

	@Test
	public void testPropietarioNoPuedeAsociarMasDeUnaVezUnInmueble() throws InmuebleYaGuardadoException {
		propietario.addInmueble(this.inmueble);

		assertThrows(InmuebleYaGuardadoException.class, () -> propietario.addInmueble(inmueble),
				"Expected exception after attempting to associate the same inmueble twice");
	}

}
