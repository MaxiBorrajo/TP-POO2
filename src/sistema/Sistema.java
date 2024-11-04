package sistema;

import sistema.managers.*;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;
import sistema.enums.FormaDePago;
import sistema.enums.RolDeUsuario;
import sistema.enums.customEnums.CustomEnum;
import sistema.enums.customEnums.CustomEnumType;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.CustomEnumExistenteException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.InmuebleConAlquilerYaExiste;
import sistema.filtro.FiltroDeSistema;

public class Sistema {

	private AlquilerManager alquilerManager;
	private ReservaManager reservaManager;
	private CustomEnumManager customEnumManager;
	private UsuarioManager usuarioManager;

	public Sistema() {
		this.alquilerManager = new AlquilerManager();
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
	public Alquiler publicarAlquiler(Inmueble inmueble, LocalTime checkIn, LocalTime checkOut, double precioDefault)
			throws InmuebleConAlquilerYaExiste {
		return this.alquilerManager.darDeAltaAlquiler(inmueble, checkIn, checkOut, precioDefault);
	}

	public List<Alquiler> buscarAlquiler(FiltroDeSistema filtro) {
		return this.alquilerManager.filtrarAlquiler(filtro);
	}

	// Reservas
	public Reserva crearReserva(FormaDePago formaDePago, LocalDate entrada, LocalDate salida, Alquiler alquiler,
			Usuario usuario) throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException {
		return this.reservaManager.crearReserva(formaDePago, entrada, salida, alquiler, usuario);
	}

	public void cancelarReserva(Reserva reserva) {

	}

	//	public List<Reserva> verTodasLasReservas(Usuario usuario){
	//	return this.reservaManager.verTodasLasReservas(Usuario usuario);
	//}
//	public List<Reserva> verReservasDeCiudad
//	public List<Reserva> verReservasFuturas
//	public List<Reserva> verCiudadesDeReservas

	// Custom enums
	public CustomEnum crearCustomEnum(String nombre, CustomEnumType tipo) throws CustomEnumExistenteException {
		return this.customEnumManager.createCustomEnum(nombre, tipo);
	}
}
