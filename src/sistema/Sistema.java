package sistema;

import sistema.managers.*;
import sistema.notificaciones.EventoNotificador;
import sistema.notificaciones.Suscriptor;
import sistema.podio.Podio;
import sistema.ranking.Rankeable;
import sistema.ranking.Ranking;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import sistema.Inmueble.Inmueble;
import sistema.Inmueble.Visualizacion;
import sistema.alquiler.Alquiler;
import sistema.alquiler.politicaDeCancelacion.PoliticaDeCancelacion;
import sistema.enums.FormaDePago;
import sistema.enums.RolDeUsuario;
import sistema.enums.customEnums.Categoria;
import sistema.enums.customEnums.CustomEnum;
import sistema.enums.customEnums.CustomEnumType;
import sistema.enums.customEnums.Servicio;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.NoExistenteException;
import sistema.exceptions.NoSuscriptoAlEvento;
import sistema.exceptions.PermisoDenegadoException;
import sistema.exceptions.ReservaNoAceptableException;
import sistema.exceptions.ReservaNoCancelableException;
import sistema.exceptions.ReservaNoTerminadaException;
import sistema.exceptions.ValoracionInvalidaException;
import sistema.exceptions.YaExistenteException;
import sistema.filtro.FiltroDeSistema;
import sistema.filtro.FiltroReserva;
import sistema.mailSender.MailSender;

public class Sistema {

	private AlquilerManager alquilerManager;
	private ReservaManager reservaManager;
	private CustomEnumManager customEnumManager;
	private UsuarioManager usuarioManager;
	private NotificadorManager notificadorManager;
	private RankingManager rankingManager;
	private MailSender mailSender;

	public Sistema(MailSender mailSender) {
		this.alquilerManager = new AlquilerManager();
		this.notificadorManager = new NotificadorManager();
		this.reservaManager = new ReservaManager();
		this.customEnumManager = new CustomEnumManager();
		this.usuarioManager = new UsuarioManager();
		this.rankingManager = new RankingManager();
		this.mailSender = mailSender;
	}

	private void validarExistenciaCustomEnum(String nombre, CustomEnumType tipo) throws NoExistenteException {
		if (!this.customEnumManager.existeCustomEnum(nombre, tipo)) {
			throw new NoExistenteException(tipo.name());
		}
	}
	
	public void cambiarPrecioAlquiler(double precio, Alquiler alquiler, Usuario propietario) throws NoExistenteException, PermisoDenegadoException {
		this.usuarioManager.validarUsuario(propietario, RolDeUsuario.PROPIETARIO);
		alquiler.cambiarPrecio(precio, notificadorManager);
	}

	public List<Ranking> getValoraciones(Rankeable rankeable) {
		// TODO Auto-generated method stub
		return this.rankingManager.getValoraciones(rankeable);
	}

	public List<Ranking> getValoracionesPorCategoria(Rankeable rankeable, Categoria categoria)
			throws NoExistenteException {
		this.validarExistenciaCustomEnum(categoria.getNombre(), CustomEnumType.CATEGORIA);
		return this.rankingManager.getValoracionesPorCategoria(rankeable, categoria);
	}

	public double getPromedioValoraciones(Rankeable rankeable) {
		return this.rankingManager.getPromedioValoraciones(rankeable);
	}

	public double getPromedioValoracionesPorCategoria(Rankeable rankeable, Categoria categoria)
			throws NoExistenteException {
		this.validarExistenciaCustomEnum(categoria.getNombre(), CustomEnumType.CATEGORIA);
		return this.rankingManager.getPromedioValoracionesPorCategoria(rankeable, categoria);
	}

	public void añadirValoracion(Ranking valoracion)
			throws ValoracionInvalidaException, NoExistenteException, ReservaNoTerminadaException {
		this.validarExistenciaCustomEnum(valoracion.getCategoria().getNombre(), CustomEnumType.CATEGORIA);
		this.rankingManager.añadirValoracion(valoracion);
	}

	public List<String> getComentarios(Rankeable rankeable) {
		// TODO Auto-generated method stub
		return this.rankingManager.getComentarios(rankeable);
	}

	public Usuario registrarUsuario(String nombreCompleto, String email, String telefono, RolDeUsuario rol)
			throws Exception {
		return this.usuarioManager.registrarUsuario(nombreCompleto, email, telefono, rol);
	}

	// Alquileres
	public Alquiler publicarAlquiler(Inmueble inmueble, LocalTime checkIn, LocalTime checkOut, double precioDefault,
			Usuario usuario, PoliticaDeCancelacion politicaDeCancelacion, List<FormaDePago> formasDePago)
			throws PermisoDenegadoException, NoExistenteException, YaExistenteException {
		this.validarExistenciaCustomEnum(inmueble.getTipo(), CustomEnumType.TIPODEINMUEBLE);
		this.validarExistenciaCustomEnums(inmueble.getServicios(), CustomEnumType.SERVICIO);
		this.usuarioManager.validarUsuario(usuario, RolDeUsuario.PROPIETARIO);
		return this.alquilerManager.darDeAltaAlquiler(inmueble, checkIn, checkOut, precioDefault, politicaDeCancelacion,
				formasDePago);
	}

	private void validarExistenciaCustomEnums(List<? extends CustomEnum> customEnums, CustomEnumType servicio)
			throws NoExistenteException {
		for (CustomEnum customEnum : customEnums) {
			this.validarExistenciaCustomEnum(customEnum.getNombre(), servicio);
		}
	}

	public List<Alquiler> buscarAlquiler(FiltroDeSistema filtro) {
		return this.alquilerManager.filtrarAlquiler(filtro);
	}

	public void aceptarReserva(Reserva reserva, Usuario propietario)
			throws PermisoDenegadoException, NoExistenteException, ReservaNoAceptableException {
		this.usuarioManager.validarUsuario(propietario, RolDeUsuario.PROPIETARIO);
		this.reservaManager.aceptarReserva(reserva, propietario, this.notificadorManager, this.mailSender);
	}

	public void rechazarReserva(Reserva reserva, Usuario propietario)
			throws NoExistenteException, PermisoDenegadoException {
		this.reservaManager.rechazarReserva(reserva, propietario);
	}

	// Reservas
	public Reserva crearReserva(FormaDePago formaDePago, LocalDate entrada, LocalDate salida, Alquiler alquiler,
			Usuario usuario, boolean esReservaCondicional) throws AlquilerNoDisponibleException,
			FormaDePagoNoAceptadaException, PermisoDenegadoException, NoExistenteException {

		this.usuarioManager.validarUsuario(usuario, RolDeUsuario.INQUILINO);
		this.alquilerManager.validarAlquiler(alquiler);
		return this.reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, usuario, esReservaCondicional);
	}

	public void cancelarReserva(Reserva reserva, Usuario inquilino)
			throws PermisoDenegadoException, NoExistenteException, AlquilerNoDisponibleException,
			FormaDePagoNoAceptadaException, ReservaNoCancelableException {
		this.reservaManager.cancelarReserva(reserva, inquilino, this.notificadorManager, this.mailSender);
	}

	public List<Reserva> verReservasSegun(FiltroReserva f) {
		return this.reservaManager.filtrarReservas(f);
	}

	public List<String> todasLasCiudadesDeReservas(Usuario user) {
		return this.reservaManager.todasLasCiudades(user);
	}

	public Visualizacion verVisualizacionDeInmueble(Inmueble inmueble) {
		return new Visualizacion(inmueble, this);
	}

	private CustomEnum crearCustomEnum(String nombre, CustomEnumType tipo, Usuario admin)
			throws PermisoDenegadoException, YaExistenteException, NoExistenteException {
		this.usuarioManager.validarUsuario(admin, RolDeUsuario.ADMINISTRADOR);
		return this.customEnumManager.createCustomEnum(nombre, tipo);
	}

	public void darAltaCategoria(String nombre, Usuario admin)
			throws PermisoDenegadoException, YaExistenteException, NoExistenteException {
		this.crearCustomEnum(nombre, CustomEnumType.CATEGORIA, admin);
	}

	public void darAltaTipoInmueble(String nombre, Usuario admin)
			throws PermisoDenegadoException, YaExistenteException, NoExistenteException {
		this.crearCustomEnum(nombre, CustomEnumType.TIPODEINMUEBLE, admin);
	}

	public void darAltaServicio(String nombre, Usuario admin)
			throws PermisoDenegadoException, YaExistenteException, NoExistenteException {
		this.crearCustomEnum(nombre, CustomEnumType.SERVICIO, admin);
	}

	public List<Podio> obtenerTop10InquilinosQueMasAlquilan(Usuario admin)
			throws PermisoDenegadoException, NoExistenteException {
		validarAdmin(admin);
		return this.reservaManager.getReservas().stream()
				.collect(Collectors.groupingBy(Reserva::getInquilino, Collectors.counting())).entrySet().stream()
				.map(entry -> new Podio(entry.getKey(), entry.getValue()))
				.sorted((a, b) -> Long.compare(b.getCantidadReservas(), a.getCantidadReservas())).limit(10)
				.collect(Collectors.toList());
	}

	public List<Inmueble> obtenerTodosLosInmueblesLibres(Usuario admin)
			throws PermisoDenegadoException, NoExistenteException {
		validarAdmin(admin);

		List<Inmueble> inmueblesOcupados = this.reservaManager.getReservasActivas().stream()
				.map(r -> r.getAlquiler().getInmueble()).toList();

		List<Inmueble> todosLosInmuebles = new ArrayList<>(
				this.alquilerManager.getAlquileres().stream().map(a -> a.getInmueble()).toList());

		todosLosInmuebles.removeAll(inmueblesOcupados);

		return todosLosInmuebles;
	}

	public double tasaDeOcupacion(Usuario admin) throws PermisoDenegadoException, NoExistenteException {
		validarAdmin(admin);

		int inmueblesOcupadosCant = (int) this.reservaManager.getReservasActivas().stream()
				.map(r -> r.getAlquiler().getInmueble()).distinct().count();

		int todosLosInmueblesCant = (int) this.alquilerManager.getAlquileres().stream().map(a -> a.getInmueble())
				.distinct().count();

		return (double) inmueblesOcupadosCant / todosLosInmueblesCant;
	}

	private void validarAdmin(Usuario usuario) throws PermisoDenegadoException, NoExistenteException {
		this.usuarioManager.validarUsuario(usuario, RolDeUsuario.ADMINISTRADOR);
	}

	public List<Inmueble> getInmuebles(Usuario propietario) {
		return this.alquilerManager.getAlquileres(propietario).stream().map(a -> a.getInmueble()).toList();
	}
	
	public void agregarSuscriptor(Suscriptor sus, EventoNotificador even) {
		this.notificadorManager.addSuscriptor(sus, even);
	}
	
	public void removerSuscriptor(Suscriptor sus, EventoNotificador even) throws NoSuscriptoAlEvento {
		this.notificadorManager.removeSuscriptor(sus, even);
	}
	
	public boolean estaSuscripto(Suscriptor sus, EventoNotificador even) throws NoSuscriptoAlEvento {
		return this.notificadorManager.estaRegistrado(sus, even);
	};
	
}
