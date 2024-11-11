package sistema.Inmueble;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.Inmueble.Inmueble;
import sistema.Inmueble.Ubicacion;
import sistema.usuario.Usuario;
import sistema.reserva.Reserva;
import sistema.alquiler.Alquiler;
import sistema.enums.RolDeUsuario;
import sistema.enums.customEnums.Servicio;
import sistema.enums.customEnums.TipoDeInmueble;

import java.util.Arrays;
import java.util.List;

public class InmuebleTest {

	private Inmueble inmueble;
	private Usuario propietario;

	@BeforeEach
	public void setUp() {
		this.propietario = mock(Usuario.class);
		when(propietario.getRol()).thenReturn(RolDeUsuario.PROPIETARIO);
		Ubicacion ubi = mock(Ubicacion.class);
		when(ubi.getCiudad()).thenReturn("Quilmes");
		when(ubi.estaEnCiudad("Quilmes")).thenReturn(true);
		when(ubi.estaEnCiudad("Bernal")).thenReturn(false);
		TipoDeInmueble inm = mock(TipoDeInmueble.class);
		when(inm.getNombre()).thenReturn("Estilo");
		this.inmueble = new Inmueble(200, inm, ubi, Arrays.asList(mock(Servicio.class)), 5, propietario);
	}

	@Test
	public void testSePuedeSaberLaCapacidadDeUnInmueble() {
		assertEquals(5, this.inmueble.getCapacidad());
	}

	@Test
	public void testSePuedeSaberLaSuperficeiDeUnInmueble() {
		assertEquals(200, this.inmueble.getSuperficie());
	}

	@Test
	public void testSePuedeSaberLaCiudadDeUnInmueble() {

		assertEquals("Quilmes", this.inmueble.getCiudad());
	}

	@Test
	public void testDevuelveLaListaDeServicios() {
		assertEquals(1, this.inmueble.getServicios().size());
	}

	@Test
	public void testSabenSIEsDeCiudad() {
		assertTrue(this.inmueble.esDeCiudad("Quilmes"));
		assertFalse(this.inmueble.esDeCiudad("Bernal"));
	}

	@Test
	public void testSabeElNombreDelTipoDeInmueble() {
		assertEquals(this.inmueble.getTipo(), "Estilo");
	}

	@Test
	public void testSabeQuienEsSuPropietario() {
		assertEquals(this.inmueble.getPropietario(), this.propietario);
	}

	@Test
	public void testSabeLasVecesAlquiladas() {
		assertEquals(this.inmueble.getVecesAlquilado(), 0);
		this.inmueble.setVecesAlquilado(this.inmueble.getVecesAlquilado() + 1);
		assertEquals(this.inmueble.getVecesAlquilado(), 1);
	}

	@Test
	public void testSonElMismoInmueble() {
		assertTrue(this.inmueble.sonElMismoInmueble(inmueble));
		assertFalse(this.inmueble.sonElMismoInmueble(new Inmueble(120, mock(TipoDeInmueble.class),
				mock(Ubicacion.class), Arrays.asList(mock(Servicio.class)), 5, propietario)));
	}

	@Test
	public void testSePuedeValorar() {
		assertFalse(this.inmueble.mePuedeValorar(propietario));
		when(propietario.getRol()).thenReturn(RolDeUsuario.INQUILINO);
		assertTrue(this.inmueble.mePuedeValorar(propietario));
	}

	@Test
	public void testSePuedeAgregarFotos() {
		this.inmueble.añadirFoto("gikas");

		assertEquals(this.inmueble.fotos().size(), 1);
		this.inmueble.añadirFoto("gikas");
		this.inmueble.añadirFoto("gikas");
		this.inmueble.añadirFoto("gikas");
		this.inmueble.añadirFoto("gikas");
		assertEquals(this.inmueble.fotos().size(), 5);
		this.inmueble.añadirFoto("gikas");
		assertEquals(this.inmueble.fotos().size(), 5);

	}

}
