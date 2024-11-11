package sistema;

import sistema.managers.*;
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
import sistema.exceptions.AlquilerNoRegistradoException;
import sistema.exceptions.CustomEnumExistenteException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.InmuebleConAlquilerYaExiste;
import sistema.exceptions.NoExistenteException;
import sistema.exceptions.PermisoDenegadoException;
import sistema.exceptions.ReservaNoCancelableException;
import sistema.exceptions.ServicioNoTerminadoException;
import sistema.exceptions.UsuarioNoRegistradoException;
import sistema.exceptions.ValoracionInvalidaException;
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
			throws ServicioNoTerminadoException, ValoracionInvalidaException, NoExistenteException {
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
			throws InmuebleConAlquilerYaExiste, UsuarioNoRegistradoException, PermisoDenegadoException,
			NoExistenteException {
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
			throws UsuarioNoRegistradoException, PermisoDenegadoException, NoExistenteException {
		this.usuarioManager.validarUsuario(propietario, RolDeUsuario.PROPIETARIO);
		this.reservaManager.aceptarReserva(reserva, propietario, this.notificadorManager, this.mailSender);
	}

	public void rechazarReserva(Reserva reserva, Usuario propietario)
			throws NoExistenteException, PermisoDenegadoException {
		// agregar validacion de propietario
		this.reservaManager.rechazarReserva(reserva, propietario);
	}

	// Reservas
	public Reserva crearReserva(FormaDePago formaDePago, LocalDate entrada, LocalDate salida, Alquiler alquiler,
			Usuario usuario, boolean esReservaCondicional)
			throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException, UsuarioNoRegistradoException,
			AlquilerNoRegistradoException, PermisoDenegadoException {

		this.usuarioManager.validarUsuario(usuario, RolDeUsuario.INQUILINO);
		this.alquilerManager.validarAlquiler(alquiler);
		return this.reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, usuario, esReservaCondicional);
	}

	public void cancelarReserva(Reserva reserva, Usuario inquilino)
			throws UsuarioNoRegistradoException, PermisoDenegadoException, NoExistenteException,
			AlquilerNoDisponibleException, FormaDePagoNoAceptadaException, ReservaNoCancelableException {
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
			throws CustomEnumExistenteException, UsuarioNoRegistradoException, PermisoDenegadoException {
		this.usuarioManager.validarUsuario(admin, RolDeUsuario.ADMINISTRADOR);
		return this.customEnumManager.createCustomEnum(nombre, tipo);
	}

	public void darAltaCategoria(String nombre, Usuario admin)
			throws CustomEnumExistenteException, UsuarioNoRegistradoException, PermisoDenegadoException {
		this.crearCustomEnum(nombre, CustomEnumType.CATEGORIA, admin);
	}

	public void darAltaTipoInmueble(String nombre, Usuario admin)
			throws CustomEnumExistenteException, UsuarioNoRegistradoException, PermisoDenegadoException {
		this.crearCustomEnum(nombre, CustomEnumType.TIPODEINMUEBLE, admin);
	}

	public void darAltaServicio(String nombre, Usuario admin)
			throws CustomEnumExistenteException, UsuarioNoRegistradoException, PermisoDenegadoException {
		this.crearCustomEnum(nombre, CustomEnumType.SERVICIO, admin);
	}

	public List<Podio> obtenerTop10InquilinosQueMasAlquilan(Usuario admin)
			throws UsuarioNoRegistradoException, PermisoDenegadoException {
		validarAdmin(admin);
		return this.reservaManager.getReservas().stream()
				.collect(Collectors.groupingBy(Reserva::getInquilino, Collectors.counting())).entrySet().stream()
				.map(entry -> new Podio(entry.getKey(), entry.getValue()))
				.sorted((a, b) -> Long.compare(b.getCantidadReservas(), a.getCantidadReservas())).limit(10)
				.collect(Collectors.toList());
	}

	public List<Inmueble> obtenerTodosLosInmueblesLibres(Usuario admin)
			throws UsuarioNoRegistradoException, PermisoDenegadoException {
		validarAdmin(admin);

		List<Inmueble> inmueblesOcupados = this.reservaManager.getReservasActivas().stream()
				.map(r -> r.getAlquiler().getInmueble()).toList();

		List<Inmueble> todosLosInmuebles = new ArrayList<>(
				this.alquilerManager.getAlquileres().stream().map(a -> a.getInmueble()).toList());

		todosLosInmuebles.removeAll(inmueblesOcupados);

		return todosLosInmuebles;
	}

	public double tasaDeOcupacion(Usuario admin) throws UsuarioNoRegistradoException, PermisoDenegadoException {
		validarAdmin(admin);

		int inmueblesOcupadosCant = (int) this.reservaManager.getReservasActivas().stream()
				.map(r -> r.getAlquiler().getInmueble()).distinct().count();

		int todosLosInmueblesCant = (int) this.alquilerManager.getAlquileres().stream().map(a -> a.getInmueble())
				.distinct().count();

		return (double) inmueblesOcupadosCant / todosLosInmueblesCant;
	}

	private void validarAdmin(Usuario usuario) throws UsuarioNoRegistradoException, PermisoDenegadoException {
		this.usuarioManager.validarUsuario(usuario, RolDeUsuario.ADMINISTRADOR);
	}

	public List<Inmueble> getInmuebles(Usuario propietario) {
		// TODO Auto-generated method stub
		return this.alquilerManager.getAlquileres(propietario).stream().map(a -> a.getInmueble()).toList();
	}

}
