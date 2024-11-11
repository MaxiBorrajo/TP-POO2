package sistema.managers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;
import sistema.alquiler.politicaDeCancelacion.PoliticaDeCancelacion;
import sistema.enums.FormaDePago;
import sistema.exceptions.NoExistenteException;
import sistema.exceptions.YaExistenteException;
import sistema.filtro.FiltroDeSistema;
import sistema.usuario.Usuario;

public class AlquilerManagerTest {

	private AlquilerManager alquilerManager;
	private Inmueble inmuebleMock;
	private PoliticaDeCancelacion politicaMock;
	private Usuario propietarioMock;
	private List<FormaDePago> formasDePago;

	@BeforeEach
	public void setUp() {
		alquilerManager = new AlquilerManager();
		inmuebleMock = mock(Inmueble.class);
		politicaMock = mock(PoliticaDeCancelacion.class);
		propietarioMock = mock(Usuario.class);
		formasDePago = Arrays.asList(FormaDePago.CREDITO, FormaDePago.EFECTIVO);

		when(inmuebleMock.getPropietario()).thenReturn(propietarioMock);
	}

	@Test
	public void testDarDeAltaAlquilerExitoso() throws YaExistenteException {
		Alquiler alquiler = alquilerManager.darDeAltaAlquiler(inmuebleMock, LocalTime.of(14, 0), LocalTime.of(10, 0),
				1000.0, politicaMock, formasDePago);
		assertTrue(alquilerManager.getAlquileres().contains(alquiler));
	}

	@Test
	public void testDarDeAltaAlquilerConInmuebleYaExistente() {

		assertDoesNotThrow(() -> {
			alquilerManager.darDeAltaAlquiler(inmuebleMock, LocalTime.of(14, 0), LocalTime.of(10, 0), 1000.0,
					politicaMock, formasDePago);
		});
		
		assertThrows(YaExistenteException.class, () -> {
			alquilerManager.darDeAltaAlquiler(inmuebleMock, LocalTime.of(16, 0), LocalTime.of(11, 0), 1200.0,
					politicaMock, formasDePago);
		});
	}

	@Test
	public void testValidarAlquilerExistente() throws YaExistenteException {
		Alquiler alquiler = alquilerManager.darDeAltaAlquiler(inmuebleMock, LocalTime.of(14, 0), LocalTime.of(10, 0),
				1000.0, politicaMock, formasDePago);

		assertDoesNotThrow(() -> alquilerManager.validarAlquiler(alquiler));
	}

	@Test
	public void testValidarAlquilerNoExistente() {
		Alquiler alquilerNoRegistrado = mock(Alquiler.class);
		assertThrows(NoExistenteException.class, () -> {
			alquilerManager.validarAlquiler(alquilerNoRegistrado);
		});
	}

	@Test
	public void testExisteAlquiler() throws YaExistenteException {
		Alquiler alquiler = alquilerManager.darDeAltaAlquiler(inmuebleMock, LocalTime.of(14, 0), LocalTime.of(10, 0),
				1000.0, politicaMock, formasDePago);

		assertTrue(alquilerManager.existeAlquiler(alquiler));
	}

	@Test
	public void testNoExisteAlquiler() {
		Alquiler alquilerNoRegistrado = mock(Alquiler.class);

		assertFalse(alquilerManager.existeAlquiler(alquilerNoRegistrado));
	}

	@Test
	public void testFiltrarAlquileres() throws YaExistenteException {
		Alquiler alquiler1 = alquilerManager.darDeAltaAlquiler(inmuebleMock, LocalTime.of(14, 0), LocalTime.of(10, 0),
				1000.0, politicaMock, formasDePago);
		Alquiler alquiler2 = mock(Alquiler.class);
		alquilerManager.getAlquileres().add(alquiler2);

		FiltroDeSistema filtro = mock(FiltroDeSistema.class);
		when(filtro.filtrarLista(anyList())).thenReturn(List.of(alquiler1));

		List<Alquiler> alquileresFiltrados = alquilerManager.filtrarAlquiler(filtro);

		assertEquals(1, alquileresFiltrados.size());
		assertEquals(alquiler1, alquileresFiltrados.get(0));
	}

	@Test
	public void testGetAlquileresPorPropietario() throws YaExistenteException {
		Alquiler alquiler = alquilerManager.darDeAltaAlquiler(inmuebleMock, LocalTime.of(14, 0), LocalTime.of(10, 0),
				1000.0, politicaMock, formasDePago);

		List<Alquiler> alquileresPropietario = alquilerManager.getAlquileres(propietarioMock);

		assertEquals(1, alquileresPropietario.size());
		assertEquals(alquiler, alquileresPropietario.get(0));
	}

	@Test
	public void testGetAlquileresSinPropietario() {
		Usuario propietarioSinAlquiler = mock(Usuario.class);
		List<Alquiler> alquileresPropietario = alquilerManager.getAlquileres(propietarioSinAlquiler);

		assertTrue(alquileresPropietario.isEmpty());
	}
}
