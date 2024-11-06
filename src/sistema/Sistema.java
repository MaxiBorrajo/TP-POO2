package sistema;

import sistema.managers.*;
import sistema.ranking.Rankeable;
import sistema.ranking.Ranking;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import sistema.Inmueble.Inmueble;
import sistema.Inmueble.Visualizacion;
import sistema.alquiler.Alquiler;
import sistema.enums.FormaDePago;
import sistema.enums.RolDeUsuario;
import sistema.enums.customEnums.Categoria;
import sistema.enums.customEnums.CustomEnum;
import sistema.enums.customEnums.CustomEnumType;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.AlquilerNoRegistradoException;
import sistema.exceptions.CustomEnumExistenteException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.InmuebleConAlquilerYaExiste;
import sistema.exceptions.NoExistenteException;
import sistema.exceptions.PermisoDenegadoException;
import sistema.exceptions.ServicioNoTerminadoException;
import sistema.exceptions.UsuarioNoRegistradoException;
import sistema.exceptions.ValoracionInvalidaException;
import sistema.filtro.FiltroDeSistema;
import sistema.filtro.FiltroReserva;
import sistema.filtro.FiltroSimple;

public class Sistema {

	private AlquilerManager alquilerManager;
	private ReservaManager reservaManager;
	private CustomEnumManager customEnumManager;
	private UsuarioManager usuarioManager;
	private NotificadorManager notificadorManager;

	public Sistema() {
		this.alquilerManager = new AlquilerManager();
		this.notificadorManager = new NotificadorManager();
		this.reservaManager = new ReservaManager();
		this.customEnumManager = new CustomEnumManager();
		this.usuarioManager = new UsuarioManager();
		
	}
	// usuarios

	public Usuario registrarUsuario(String nombreCompleto, String email, String telefono, RolDeUsuario rol)
			throws Exception {
		return this.usuarioManager.registrarUsuario(nombreCompleto, email, telefono, rol);
	}

	// Alquileres
	public Alquiler publicarAlquiler(Inmueble inmueble, LocalTime checkIn, LocalTime checkOut, double precioDefault,
			Usuario usuario)
			throws InmuebleConAlquilerYaExiste, UsuarioNoRegistradoException, PermisoDenegadoException {
		this.usuarioManager.validarUsuario(usuario, RolDeUsuario.PROPIETARIO);
		return this.alquilerManager.darDeAltaAlquiler(inmueble, checkIn, checkOut, precioDefault);
	}

	public List<Alquiler> buscarAlquiler(FiltroDeSistema filtro) {
		return this.alquilerManager.filtrarAlquiler(filtro);
	}
	
	public void aceptarReserva(Reserva reser) {
		this.reservaManager.aceptarReserva(reser, this.notificadorManager);
	}

	public void rechazarReserva(Reserva reser) {
		this.reservaManager.rechazarReserva(reser);
	}
	
	// Reservas
	public Reserva crearReserva(FormaDePago formaDePago, LocalDate entrada, LocalDate salida, Alquiler alquiler,
			Usuario usuario) throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException,
			UsuarioNoRegistradoException, AlquilerNoRegistradoException, PermisoDenegadoException {
		this.usuarioManager.validarUsuario(usuario, RolDeUsuario.INQUILINO);
		this.alquilerManager.validarAlquiler(alquiler);
		return this.reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, usuario);
	}

	public void cancelarReserva(Reserva reserva) throws UsuarioNoRegistradoException, PermisoDenegadoException,
			NoExistenteException, AlquilerNoDisponibleException, FormaDePagoNoAceptadaException {
		this.usuarioManager.validarUsuario(reserva.getInquilino(), RolDeUsuario.INQUILINO);
		this.reservaManager.cancelarReserva(reserva, this.notificadorManager);
	}

	public List<Reserva> verReservasSegun(FiltroReserva f) {
		return this.reservaManager.filtrarReservas(f);
	}

	public List<String> todasLasCiudadesDeReservas(Usuario user) {
		return this.reservaManager.todasLasCiudades(user);
	}

	public Visualizacion verVisualizacionDeInmueble(Inmueble inmueble) {
		return new Visualizacion(inmueble);
	}

	// public List<Reserva> verTodasLasReservas(Usuario usuario){
	// return this.reservaManager.verTodasLasReservas(Usuario usuario);
	// }
//	public List<Reserva> verReservasDeCiudad
//	public List<Reserva> verReservasFuturas
//	public List<Reserva> verCiudadesDeReservas

	// Custom enums
	public CustomEnum crearCustomEnum(String nombre, CustomEnumType tipo) throws CustomEnumExistenteException {
		return this.customEnumManager.createCustomEnum(nombre, tipo);
	}

}
