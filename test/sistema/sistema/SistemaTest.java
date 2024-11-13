package sistema.sistema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.*;
import sistema.Inmueble.Inmueble;
import sistema.Inmueble.Visualizacion;
import sistema.alquiler.Alquiler;
import sistema.alquiler.politicaDeCancelacion.PoliticaDeCancelacion;
import sistema.enums.FormaDePago;
import sistema.enums.RolDeUsuario;
import sistema.enums.customEnums.Categoria;
import sistema.enums.customEnums.CustomEnum;
import sistema.enums.customEnums.CustomEnumType;
import sistema.enums.customEnums.TipoDeInmueble;
import sistema.exceptions.*;
import sistema.filtro.FiltroDeSistema;
import sistema.filtro.FiltroReserva;
import sistema.filtro.FiltroReservasFuturas;
import sistema.filtro.FiltroSimple;
import sistema.filtro.FiltroTodasReservas;
import sistema.mailSender.MailSender;
import sistema.managers.NotificadorManager;
import sistema.managers.ReservaManager;
import sistema.notificaciones.BajaPrecioNotify;
import sistema.notificaciones.EventoNotificador;
import sistema.notificaciones.Suscriptor;
import sistema.podio.Podio;
import sistema.ranking.Rankeable;
import sistema.ranking.Ranking;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SistemaTest {

	private Sistema sistema;
	private Usuario admin;
	private Usuario noAdmin;
	private Usuario propietario;

	@BeforeEach
	public void setUp() {
		MailSender mailSender = mock(MailSender.class);
		sistema = new Sistema(mailSender);
		try {
			admin = sistema.registrarUsuario("Admin", "admin@example.com", "123456", RolDeUsuario.ADMINISTRADOR);
			noAdmin = sistema.registrarUsuario("User", "user@example.com", "654321", RolDeUsuario.INQUILINO);
			propietario = sistema.registrarUsuario("Propietario", "propietario@example.com", "789012",
					RolDeUsuario.PROPIETARIO);
			sistema.darAltaTipoInmueble("Casa", admin);
			sistema.darAltaCategoria("Limpieza", admin);
			sistema.darAltaCategoria("Servicio", admin);
		} catch (Exception e) {
			fail("Error al registrar usuarios: " + e.getMessage());
		}
	}

	@Test
	public void testAñadirValoracion() throws ValoracionInvalidaException, RangoValoracionInvalidoException {
		Rankeable rankeable = mock(Rankeable.class);
		Usuario usuario = mock(Usuario.class);
		Categoria categoria = new Categoria("Limpieza");
		Reserva reserva = mock(Reserva.class);
		when(reserva.estaFinalizada()).thenReturn(true);
		when(rankeable.mePuedeValorar(usuario)).thenReturn(true);

		Ranking valoracion = new Ranking(5, categoria, "Excelente", reserva, rankeable, usuario);

		assertDoesNotThrow(() -> sistema.añadirValoracion(valoracion));
		List<Ranking> valoraciones = sistema.getValoraciones(rankeable);
		assertTrue(valoraciones.contains(valoracion));
	}

	@Test
	public void testGetValoracionesPorCategoria() throws ValoracionInvalidaException, RangoValoracionInvalidoException,
			NoExistenteException, ReservaNoTerminadaException {
		Rankeable rankeable = mock(Rankeable.class);
		Usuario usuario = mock(Usuario.class);
		Categoria categoria = new Categoria("Servicio");
		Categoria categoriaDiferente = new Categoria("Limpieza");
		Reserva reserva = mock(Reserva.class);

		when(reserva.estaFinalizada()).thenReturn(true);
		when(rankeable.mePuedeValorar(usuario)).thenReturn(true);

		Ranking valoracion1 = new Ranking(4, categoria, "Buen servicio", reserva, rankeable, usuario);
		Ranking valoracion2 = new Ranking(5, categoriaDiferente, "Excelente limpieza", reserva, rankeable, usuario);

		sistema.añadirValoracion(valoracion1);
		sistema.añadirValoracion(valoracion2);

		List<Ranking> valoracionesServicio = sistema.getValoracionesPorCategoria(rankeable, categoria);
		assertEquals(1, valoracionesServicio.size());
		assertTrue(valoracionesServicio.contains(valoracion1));
		assertFalse(valoracionesServicio.contains(valoracion2));
	}

	@Test
	public void testGetPromedioValoraciones() throws ValoracionInvalidaException, RangoValoracionInvalidoException,
			NoExistenteException, ReservaNoTerminadaException {
		Rankeable rankeable = mock(Rankeable.class);
		Usuario usuario = mock(Usuario.class);
		Categoria categoria = new Categoria("Servicio");
		Reserva reserva = mock(Reserva.class);

		when(reserva.estaFinalizada()).thenReturn(true);
		when(rankeable.mePuedeValorar(usuario)).thenReturn(true);

		Ranking valoracion1 = new Ranking(3, categoria, "Regular", reserva, rankeable, usuario);
		Ranking valoracion2 = new Ranking(5, categoria, "Excelente", reserva, rankeable, usuario);

		sistema.añadirValoracion(valoracion1);
		sistema.añadirValoracion(valoracion2);

		double promedio = sistema.getPromedioValoraciones(rankeable);
		assertEquals(4.0, promedio);
	}

	@Test
	public void testGetPromedioValoracionesPorCategoria() throws ReservaNoTerminadaException,
			ValoracionInvalidaException, RangoValoracionInvalidoException, NoExistenteException {
		Rankeable rankeable = mock(Rankeable.class);
		Usuario usuario = mock(Usuario.class);
		Categoria categoriaServicio = new Categoria("Servicio");
		Categoria categoriaLimpieza = new Categoria("Limpieza");
		Reserva reserva = mock(Reserva.class);

		when(reserva.estaFinalizada()).thenReturn(true);
		when(rankeable.mePuedeValorar(usuario)).thenReturn(true);

		Ranking valoracion1 = new Ranking(5, categoriaServicio, "Excelente servicio", reserva, rankeable, usuario);
		Ranking valoracion2 = new Ranking(3, categoriaLimpieza, "Regular limpieza", reserva, rankeable, usuario);
		Ranking valoracion3 = new Ranking(4, categoriaServicio, "Buen servicio", reserva, rankeable, usuario);

		sistema.añadirValoracion(valoracion1);
		sistema.añadirValoracion(valoracion2);
		sistema.añadirValoracion(valoracion3);

		double promedioServicio = sistema.getPromedioValoracionesPorCategoria(rankeable, categoriaServicio);
		assertEquals(4.5, promedioServicio);
	}

	@Test
	public void testGetComentarios() throws ReservaNoTerminadaException, ValoracionInvalidaException,
			RangoValoracionInvalidoException, NoExistenteException {
		Rankeable rankeable = mock(Rankeable.class);
		Usuario usuario = mock(Usuario.class);
		Categoria categoria = new Categoria("Servicio");
		Reserva reserva = mock(Reserva.class);

		when(reserva.estaFinalizada()).thenReturn(true);
		when(rankeable.mePuedeValorar(usuario)).thenReturn(true);

		Ranking valoracion1 = new Ranking(4, categoria, "Buen servicio", reserva, rankeable, usuario);
		Ranking valoracion2 = new Ranking(5, categoria, "Excelente atención", reserva, rankeable, usuario);

		sistema.añadirValoracion(valoracion1);
		sistema.añadirValoracion(valoracion2);

		List<String> comentarios = sistema.getComentarios(rankeable);
		assertEquals(2, comentarios.size());
		assertTrue(comentarios.contains("Buen servicio"));
		assertTrue(comentarios.contains("Excelente atención"));
	}

	@Test
	public void testRegistrarUsuarioExitoso() throws Exception {
		Usuario nuevoUsuario = sistema.registrarUsuario("John Doe", "johndoe@example.com", "123456789",
				RolDeUsuario.INQUILINO);

		assertNotNull(nuevoUsuario);
		assertEquals("John Doe", nuevoUsuario.getNombre());
		assertEquals("johndoe@example.com", nuevoUsuario.getEmail());
		assertEquals("123456789", nuevoUsuario.getTelefono());
		assertEquals(RolDeUsuario.INQUILINO, nuevoUsuario.getRol());
	}

	@Test
	public void testRegistrarUsuarioYaExistente() {
		assertDoesNotThrow(() -> sistema.registrarUsuario("Jane Doe", "janedoe@example.com", "987654321",
				RolDeUsuario.PROPIETARIO));

		assertThrows(YaExistenteException.class, () -> sistema.registrarUsuario("Jane Doe", "janedoe@example.com",
				"987654321", RolDeUsuario.PROPIETARIO));
	}

	@Test
	public void testDarAltaCategoriaConAdmin() {
		assertDoesNotThrow(() -> sistema.darAltaCategoria("CategoriaNueva", admin));
		assertThrows(YaExistenteException.class, () -> sistema.darAltaCategoria("CategoriaNueva", admin));
	}

	@Test
	public void testDarAltaCategoriaConUsuarioNoAdmin() {
		assertThrows(PermisoDenegadoException.class, () -> sistema.darAltaCategoria("CategoriaNueva", noAdmin));
	}

	@Test
	public void testDarAltaTipoInmuebleConAdmin() {
		assertDoesNotThrow(() -> sistema.darAltaTipoInmueble("TipoInmuebleNuevo", admin));
		assertThrows(YaExistenteException.class, () -> sistema.darAltaTipoInmueble("TipoInmuebleNuevo", admin));
	}

	@Test
	public void testDarAltaTipoInmuebleConUsuarioNoAdmin() {
		assertThrows(PermisoDenegadoException.class, () -> sistema.darAltaTipoInmueble("TipoInmuebleNuevo", noAdmin));
	}

	@Test
	public void testDarAltaServicioConAdmin() {
		assertDoesNotThrow(() -> sistema.darAltaServicio("ServicioNuevo", admin));
		assertThrows(YaExistenteException.class, () -> sistema.darAltaServicio("ServicioNuevo", admin));
	}

	@Test
	public void testDarAltaServicioConUsuarioNoAdmin() {
		assertThrows(PermisoDenegadoException.class, () -> sistema.darAltaServicio("ServicioNuevo", noAdmin));
	}

	@Test
	public void testObtenerTop10InquilinosQueMasAlquilanConAdmin() throws Exception {
		Usuario inquilino1 = sistema.registrarUsuario("Inquilino1", "inquilino1@example.com", "134",
				RolDeUsuario.INQUILINO);
		Usuario inquilino2 = sistema.registrarUsuario("Inquilino2", "inquilino2@example.com", "134",
				RolDeUsuario.INQUILINO);

		Inmueble inmueble1 = mock(Inmueble.class);
		Inmueble inmueble2 = mock(Inmueble.class);
		when(inmueble1.getTipo()).thenReturn("Casa");
		when(inmueble2.getTipo()).thenReturn("Casa");
		when(inmueble1.getPropietario()).thenReturn(propietario);
		when(inmueble2.getPropietario()).thenReturn(propietario);
		Alquiler alquiler1 = sistema.publicarAlquiler(inmueble2, LocalTime.of(9, 0), LocalTime.of(17, 0), 400,
				propietario, mock(PoliticaDeCancelacion.class), Arrays.asList(FormaDePago.CREDITO));
		Alquiler alquiler2 = sistema.publicarAlquiler(inmueble1, LocalTime.of(10, 0), LocalTime.of(18, 0), 500,
				propietario, mock(PoliticaDeCancelacion.class), Arrays.asList(FormaDePago.CREDITO));

		sistema.crearReserva(FormaDePago.CREDITO, LocalDate.now(), LocalDate.now().plusDays(5), alquiler1, inquilino1,
				false);
		sistema.crearReserva(FormaDePago.CREDITO, LocalDate.now(), LocalDate.now().plusDays(3), alquiler2, inquilino2,
				false);
		sistema.crearReserva(FormaDePago.CREDITO, LocalDate.now().plusDays(10), LocalDate.now().plusDays(13), alquiler2,
				inquilino1, false);

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

		when(inmueble1.getTipo()).thenReturn("Casa");
		when(inmueble2.getTipo()).thenReturn("Casa");
		when(inmueble1.getPropietario()).thenReturn(propietario);
		when(inmueble2.getPropietario()).thenReturn(propietario);

		Alquiler alquiler1 = sistema.publicarAlquiler(inmueble1, LocalTime.of(10, 0), LocalTime.of(18, 0), 500,
				propietario, mock(PoliticaDeCancelacion.class), Arrays.asList(FormaDePago.CREDITO));
		Alquiler alquiler2 = sistema.publicarAlquiler(inmueble2, LocalTime.of(9, 0), LocalTime.of(17, 0), 400,
				propietario, mock(PoliticaDeCancelacion.class), Arrays.asList(FormaDePago.CREDITO));

		Reserva reserva = sistema.crearReserva(FormaDePago.CREDITO, LocalDate.now(), LocalDate.now().plusDays(5),
				alquiler1, noAdmin, false);
		sistema.aceptarReserva(reserva, propietario);

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
		when(inmueble1.getTipo()).thenReturn("Casa");
		when(inmueble2.getTipo()).thenReturn("Casa");
		when(inmueble1.getPropietario()).thenReturn(propietario);
		when(inmueble2.getPropietario()).thenReturn(propietario);
		Alquiler alquiler1 = sistema.publicarAlquiler(inmueble1, LocalTime.of(10, 0), LocalTime.of(18, 0), 500,
				propietario, mock(PoliticaDeCancelacion.class), Arrays.asList(FormaDePago.CREDITO));
		Alquiler alquiler2 = sistema.publicarAlquiler(inmueble2, LocalTime.of(9, 0), LocalTime.of(17, 0), 400,
				propietario, mock(PoliticaDeCancelacion.class), Arrays.asList(FormaDePago.CREDITO));

		Reserva reserva = sistema.crearReserva(FormaDePago.CREDITO, LocalDate.now(), LocalDate.now().plusDays(5),
				alquiler1, noAdmin, false);
		sistema.aceptarReserva(reserva, propietario);
		double tasaOcupacion = sistema.tasaDeOcupacion(admin);

		assertEquals(0.5, tasaOcupacion);
	}

	@Test
	public void testTasaDeOcupacionConUsuarioNoAdmin() {
		assertThrows(PermisoDenegadoException.class, () -> sistema.tasaDeOcupacion(noAdmin));
	}

	@Test
	public void testPublicarAlquiler() throws Exception {
		Inmueble inmueble = mock(Inmueble.class);
		when(inmueble.getTipo()).thenReturn("Casa");
		when(inmueble.getPropietario()).thenReturn(propietario);
		PoliticaDeCancelacion politica = mock(PoliticaDeCancelacion.class);
		Alquiler alquiler = sistema.publicarAlquiler(inmueble, LocalTime.of(10, 0), LocalTime.of(18, 0), 500.0,
				propietario, politica, Arrays.asList(FormaDePago.CREDITO));

		assertNotNull(alquiler);
		assertEquals(inmueble, alquiler.getInmueble());
		assertEquals(500.0, alquiler.getPrecioBase());
	}

	@Test
	public void testPublicarAlquilerConCategoriasInvalidas() throws Exception {
		// Arrange: mock inputs and behavior
		Inmueble inmueble = mock(Inmueble.class);
		when(inmueble.getTipo()).thenReturn("invalido");
		when(inmueble.getPropietario()).thenReturn(propietario);

		PoliticaDeCancelacion politica = mock(PoliticaDeCancelacion.class);
		List<FormaDePago> formasDePago = Arrays.asList(FormaDePago.CREDITO);

		// Act and Assert
		assertThrows(NoExistenteException.class, () -> sistema.publicarAlquiler(inmueble, LocalTime.of(10, 0),
				LocalTime.of(18, 0), 500.0, propietario, politica, formasDePago));
	}

	@Test
	public void testCancelarReserva() throws Exception {
		Inmueble inmueble = mock(Inmueble.class);
		when(inmueble.getTipo()).thenReturn("Casa");
		when(inmueble.getPropietario()).thenReturn(propietario);

		Alquiler alquiler = sistema.publicarAlquiler(inmueble, LocalTime.of(10, 0), LocalTime.of(18, 0), 500.0,
				propietario, mock(PoliticaDeCancelacion.class), Arrays.asList(FormaDePago.CREDITO));

		Reserva reserva = sistema.crearReserva(FormaDePago.CREDITO, LocalDate.now(), LocalDate.now().plusDays(5),
				alquiler, noAdmin, false);

		assertNotNull(reserva);

		sistema.aceptarReserva(reserva, propietario);

		sistema.cancelarReserva(reserva, noAdmin);

		assertFalse(reserva.estaActiva());
	}

	@Test
	public void testCancelarReservaNoExistente() throws Exception {
		Reserva reserva = mock(Reserva.class);
		Usuario usuario = new Usuario("Inquilino", "inquilino@example.com", "123456789", RolDeUsuario.INQUILINO);

		sistema.registrarUsuario(usuario.getNombre(), usuario.getEmail(), usuario.getTelefono(), usuario.getRol());

		when(reserva.getInquilino()).thenReturn(usuario);

		assertThrows(NoExistenteException.class, () -> sistema.cancelarReserva(reserva, usuario));
	}

	@Test
	public void testCrearReservaConFormaDePagoNoAceptada() throws Exception {
		Inmueble inmueble = mock(Inmueble.class);
		when(inmueble.getTipo()).thenReturn("Casa");
		when(inmueble.getPropietario()).thenReturn(propietario);
		Alquiler alquiler = sistema.publicarAlquiler(inmueble, LocalTime.of(10, 0), LocalTime.of(18, 0), 500.0,
				propietario, mock(PoliticaDeCancelacion.class), Arrays.asList(FormaDePago.CREDITO));

		assertThrows(FormaDePagoNoAceptadaException.class, () -> {
			sistema.crearReserva(FormaDePago.EFECTIVO, LocalDate.now(), LocalDate.now().plusDays(3), alquiler, noAdmin,
					false);
		});
	}

	@Test
	public void testVisualizacionDeInmueble() {
		Inmueble inmueble = mock(Inmueble.class);
		Visualizacion visualizacion = sistema.verVisualizacionDeInmueble(inmueble);

		assertNotNull(visualizacion);
		assertEquals(inmueble, visualizacion.getInmueble());
	}

	@Test
	public void testValidacionDeEnumNoExistente() {
		Rankeable rankeable = mock(Rankeable.class);
		Categoria categoria = mock(Categoria.class);

		String nonExistentCategoryName = "NonExistentCategory";
		when(categoria.getNombre()).thenReturn(nonExistentCategoryName);

		assertThrows(NoExistenteException.class, () -> sistema.getValoracionesPorCategoria(rankeable, categoria));

	}

	@Test
	public void testVerReservasSegunFiltro() {
		FiltroReserva fr = mock(FiltroTodasReservas.class);

		when(fr.filtrarReservas(Arrays.asList())).thenReturn(Arrays.asList());

		assertEquals(this.sistema.verReservasSegun(fr).size(), 0);
	}

	@Test
	public void testTodasLasCiudades() {

		assertEquals(this.sistema.todasLasCiudadesDeReservas(noAdmin), Arrays.asList());
	}

	@Test
	public void testVerTodasLasCiudades()
			throws PermisoDenegadoException, NoExistenteException, YaExistenteException, AlquilerNoDisponibleException,
			FormaDePagoNoAceptadaException, ReservaNoCancelableException, ReservaNoAceptableException {
		Inmueble inmueble = mock(Inmueble.class);
		when(inmueble.getTipo()).thenReturn("Casa");
		when(inmueble.getPropietario()).thenReturn(propietario);
		when(inmueble.getCiudad()).thenReturn("Quilmes");

		Alquiler alquiler = sistema.publicarAlquiler(inmueble, LocalTime.of(10, 0), LocalTime.of(18, 0), 500.0,
				propietario, mock(PoliticaDeCancelacion.class), Arrays.asList(FormaDePago.CREDITO));

		Reserva reserva = sistema.crearReserva(FormaDePago.CREDITO, LocalDate.now(), LocalDate.now().plusDays(5),
				alquiler, noAdmin, false);

		sistema.aceptarReserva(reserva, propietario);

		sistema.cancelarReserva(reserva, noAdmin);

		assertEquals(this.sistema.todasLasCiudadesDeReservas(noAdmin), Arrays.asList("Quilmes"));
	}

	@Test
	public void testSePuedeFiltrarPorAlquiler() {
		FiltroDeSistema fs = mock(FiltroDeSistema.class);
		when(fs.filtrarLista(Arrays.asList())).thenReturn(Arrays.asList());

		assertEquals(this.sistema.buscarAlquiler(fs), Arrays.asList());

	}
	
	@Test
	public void testSePuedenSuscribirYremoverDeEventos() throws NoSuscriptoAlEvento {
		Suscriptor sus = mock(Suscriptor.class);
		EventoNotificador even = mock(BajaPrecioNotify.class);
		
		
		this.sistema.agregarSuscriptor(sus, even);
		when(even.esIgualA(even)).thenReturn(true);
		assertTrue(this.sistema.estaSuscripto(sus, even));
		this.sistema.removerSuscriptor(sus, even);
		assertFalse(this.sistema.estaSuscripto(sus, even));
	}
	
	@Test
	public void testRechazarReserva() throws NoSuscriptoAlEvento, NoExistenteException, PermisoDenegadoException {
		Reserva re = mock(Reserva.class);
		when(re.getPropietario()).thenReturn(propietario);
		this.sistema.rechazarReserva(re, propietario);;
		verify(re).rechazar();
	}
	
	@Test
	public void testBajarPrecio() throws NoSuscriptoAlEvento, NoExistenteException, PermisoDenegadoException {
		Alquiler alq = mock(Alquiler.class);
		
		this.sistema.cambiarPrecioAlquiler(199d, alq, propietario);;
		verify(alq).cambiarPrecio(eq(199d), any(NotificadorManager.class));
	}
}
