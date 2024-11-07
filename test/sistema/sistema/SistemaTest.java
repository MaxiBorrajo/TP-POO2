package sistema.sistema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.*;
import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;
import sistema.alquiler.politicaDeCancelacion.PoliticaDeCancelacion;
import sistema.enums.FormaDePago;
import sistema.enums.RolDeUsuario;
import sistema.exceptions.*;
import sistema.filtro.FiltroSimple;
import sistema.podio.Podio;
import sistema.reserva.Reserva;
import sistema.usuario.Administrador;
import sistema.usuario.Inquilino;
import sistema.usuario.Propietario;
import sistema.usuario.Usuario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SistemaTest {

	private Sistema sistema;
	private Usuario admin;
	private Inquilino noAdmin;
	private Propietario propietario;

	@BeforeEach
	public void setUp() {
		sistema = new Sistema();
		admin = new Administrador("Admin", "admin@example.com", "123456");
		noAdmin = new Inquilino("User", "user@example.com", "654321");
		propietario = new Propietario("Propietario", "propietario@example.com", "789012", LocalDate.now());
		try {
			sistema.registrarUsuario(admin.getNombre(), admin.getEmail(), admin.getTelefono(), admin.getRol());
			sistema.registrarUsuario(noAdmin.getNombre(), noAdmin.getEmail(), noAdmin.getTelefono(), noAdmin.getRol());
			sistema.registrarUsuario(propietario.getNombre(), propietario.getEmail(), propietario.getTelefono(),
					propietario.getRol());
		} catch (Exception e) {
			fail("Error al registrar usuarios: " + e.getMessage());
		}
	}

	@Test
	public void testDarAltaCategoriaConAdmin() {
		assertDoesNotThrow(() -> sistema.darAltaCategoria("CategoriaNueva", admin));
		assertThrows(CustomEnumExistenteException.class, () -> sistema.darAltaCategoria("CategoriaNueva", admin));
	}

	@Test
	public void testDarAltaCategoriaConUsuarioNoAdmin() {
		assertThrows(PermisoDenegadoException.class, () -> sistema.darAltaCategoria("CategoriaNueva", noAdmin));
	}

	@Test
	public void testDarAltaTipoInmuebleConAdmin() {
		assertDoesNotThrow(() -> sistema.darAltaTipoInmueble("TipoInmuebleNuevo", admin));
		assertThrows(CustomEnumExistenteException.class, () -> sistema.darAltaTipoInmueble("TipoInmuebleNuevo", admin));
	}

	@Test
	public void testDarAltaTipoInmuebleConUsuarioNoAdmin() {
		assertThrows(PermisoDenegadoException.class, () -> sistema.darAltaTipoInmueble("TipoInmuebleNuevo", noAdmin));
	}

	@Test
	public void testDarAltaServicioConAdmin() {
		assertDoesNotThrow(() -> sistema.darAltaServicio("ServicioNuevo", admin));
		assertThrows(CustomEnumExistenteException.class, () -> sistema.darAltaServicio("ServicioNuevo", admin));
	}

	@Test
	public void testDarAltaServicioConUsuarioNoAdmin() {
		assertThrows(PermisoDenegadoException.class, () -> sistema.darAltaServicio("ServicioNuevo", noAdmin));
	}

	@Test
	public void testObtenerTop10InquilinosQueMasAlquilanConAdmin() throws Exception {
		Inquilino inquilino1 = new Inquilino("Inquilino1", "inquilino1@example.com", "134");
		Inquilino inquilino2 = new Inquilino("Inquilino2", "inquilino2@example.com", "134");
		sistema.registrarUsuario(inquilino1.getNombre(), inquilino1.getEmail(), inquilino1.getTelefono(),
				inquilino1.getRol());
		sistema.registrarUsuario(inquilino2.getNombre(), inquilino2.getEmail(), inquilino2.getTelefono(),
				inquilino2.getRol());

		Inmueble inmueble1 = mock(Inmueble.class);
		Inmueble inmueble2 = mock(Inmueble.class);

		Alquiler alquiler1 = sistema.publicarAlquiler(inmueble2, LocalTime.of(9, 0), LocalTime.of(17, 0), 400,
				propietario, mock(PoliticaDeCancelacion.class));
		Alquiler alquiler2 = sistema.publicarAlquiler(inmueble1, LocalTime.of(10, 0), LocalTime.of(18, 0), 500,
				propietario, mock(PoliticaDeCancelacion.class));

		alquiler1.agregarFormaDePago(FormaDePago.CREDITO);
		alquiler2.agregarFormaDePago(FormaDePago.CREDITO);

		sistema.crearReserva(FormaDePago.CREDITO, LocalDate.now(), LocalDate.now().plusDays(5), alquiler1, inquilino1);
		sistema.crearReserva(FormaDePago.CREDITO, LocalDate.now(), LocalDate.now().plusDays(3), alquiler2, inquilino2);
		sistema.crearReserva(FormaDePago.CREDITO, LocalDate.now().plusDays(10), LocalDate.now().plusDays(13), alquiler2,
				inquilino1);

		List<Podio> topInquilinos = sistema.obtenerTop10InquilinosQueMasAlquilan(admin);
		assertEquals(2, topInquilinos.size());
		assertEquals(inquilino1.getNombre(), topInquilinos.get(0).getInquilino().getNombre());
		assertEquals(inquilino1.getEmail(), topInquilinos.get(0).getInquilino().getEmail());
	}

	@Test
	public void testObtenerTop10InquilinosQueMasAlquilanConNoAdmin() {
		assertThrows(PermisoDenegadoException.class, () -> sistema.obtenerTop10InquilinosQueMasAlquilan(noAdmin));
	}

	@Test
	public void testObtenerTodosLosInmueblesLibresConAdmin() throws Exception {
		Inmueble inmueble1 = mock(Inmueble.class);
		Inmueble inmueble2 = mock(Inmueble.class);

		Alquiler alquiler1 = sistema.publicarAlquiler(inmueble1, LocalTime.of(10, 0), LocalTime.of(18, 0), 500,
				propietario, mock(PoliticaDeCancelacion.class));
		Alquiler alquiler2 = sistema.publicarAlquiler(inmueble2, LocalTime.of(9, 0), LocalTime.of(17, 0), 400,
				propietario, mock(PoliticaDeCancelacion.class));

		alquiler1.agregarFormaDePago(FormaDePago.CREDITO);

		sistema.crearReserva(FormaDePago.CREDITO, LocalDate.now(), LocalDate.now().plusDays(5), alquiler1, noAdmin);

		List<Inmueble> inmueblesLibres = sistema.obtenerTodosLosInmueblesLibres(admin);
		assertEquals(1, inmueblesLibres.size());
		assertEquals(inmueble2, inmueblesLibres.get(0));
		assertFalse(inmueblesLibres.contains(inmueble1));
	}

	@Test
	public void testObtenerTodosLosInmueblesLibresConUsuarioNoAdmin() {
		assertThrows(PermisoDenegadoException.class, () -> sistema.obtenerTodosLosInmueblesLibres(noAdmin));
	}

	@Test
	public void testTasaDeOcupacionConAdmin() throws Exception {
		Inmueble inmueble1 = mock(Inmueble.class);
		Inmueble inmueble2 = mock(Inmueble.class);

		Alquiler alquiler1 = sistema.publicarAlquiler(inmueble1, LocalTime.of(10, 0), LocalTime.of(18, 0), 500,
				propietario, mock(PoliticaDeCancelacion.class));
		Alquiler alquiler2 = sistema.publicarAlquiler(inmueble2, LocalTime.of(9, 0), LocalTime.of(17, 0), 400,
				propietario, mock(PoliticaDeCancelacion.class));

		alquiler1.agregarFormaDePago(FormaDePago.CREDITO);

		sistema.crearReserva(FormaDePago.CREDITO, LocalDate.now(), LocalDate.now().plusDays(5), alquiler1, noAdmin);

		double tasaOcupacion = sistema.tasaDeOcupacion(admin);

		assertEquals(0.5, tasaOcupacion);
	}

	@Test
	public void testTasaDeOcupacionConUsuarioNoAdmin() {
		assertThrows(PermisoDenegadoException.class, () -> sistema.tasaDeOcupacion(noAdmin));
	}
}
