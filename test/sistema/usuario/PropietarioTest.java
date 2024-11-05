package sistema.usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;
import sistema.exceptions.InmuebleYaGuardadoException;
import sistema.reserva.Reserva;

public class PropietarioTest {
	private Propietario propietario;
	private Usuario mockRanker;
	private Reserva mockReserva;
	private Inmueble mockInmueble;

	@BeforeEach
	void setUp() {
		this.propietario = new Propietario("ginebra", "ginebra@gmail.com", "+5401166666666", LocalDate.now());
		mockRanker = mock(Usuario.class);
		mockReserva = mock(Reserva.class);
		mockInmueble = mock(Inmueble.class);
		Alquiler mockAlquiler = mock(Alquiler.class);
		when(mockReserva.getAlquiler()).thenReturn(mockAlquiler);
		when(mockInmueble.getPropietario()).thenReturn(propietario);
		when(mockAlquiler.getInmueble()).thenReturn(mockInmueble);
	}

	@Test
	public void testPropietarioPuedeAsociarUnInmueble() throws InmuebleYaGuardadoException {
		assertEquals(0, propietario.getInmuebles().size(), "Initial Inmueble count should be 0");

		propietario.addInmueble(this.mockInmueble);

		assertEquals(1, propietario.getInmuebles().size(), "Inmueble count should be 1");
		assertTrue(propietario.tieneInmueble(this.mockInmueble));
	}

	@Test
	public void testPropietarioNoPuedeAsociarMasDeUnaVezUnInmueble() throws InmuebleYaGuardadoException {
		propietario.addInmueble(this.mockInmueble);

		assertThrows(InmuebleYaGuardadoException.class, () -> propietario.addInmueble(mockInmueble),
				"Expected exception after attempting to associate the same inmueble twice");
	}

	@Test
	public void testEsValoracionValidaCasoValido() {
		when(mockReserva.getInquilino()).thenReturn(mockRanker);

		boolean resultado = propietario.esValoracionValida(mockRanker, mockReserva);

		assertTrue(resultado,
				"El método debería devolver true cuando el ranker es el inquilino y el propietario coincide con 'this'");
	}

	@Test
	public void testEsValoracionValidaCasoInvalido() {
		Usuario otroRanker = mock(Usuario.class);
		when(mockReserva.getInquilino()).thenReturn(otroRanker);

		boolean resultado = propietario.esValoracionValida(mockRanker, mockReserva);

		assertFalse(resultado,
				"El método debería devolver false cuando el ranker no es el inquilino o el propietario no coincide con 'this'");
	}

}
