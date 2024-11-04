package sistema.reserva;
import java.time.LocalDate;

import sistema.alquiler.Alquiler;
import sistema.enums.*;
import sistema.managers.ReservaManager;
import sistema.usuario.Usuario;
public class Reserva {
	private EstadoDeReserva estado;
	private FormaDePago formaDepago;
	private LocalDate fechaInicio;
	private LocalDate fechaFinal;
	private Alquiler alquiler;
	private Usuario inquilino;
	
	public Reserva(FormaDePago formaDepago, LocalDate fechaInicio, LocalDate fechaFinal, Alquiler alquiler, Usuario usuario) {
	    this.estado = EstadoDeReserva.PENDIENTE;
	    this.formaDepago = formaDepago;
	    this.fechaInicio = fechaInicio;
	    this.fechaFinal = fechaFinal;
	    this.alquiler = alquiler;
	    this.inquilino = usuario;
	}

	public Usuario getInquilino() {
		return this.inquilino;
	}

	public boolean esDeUsuario(Usuario us) {
		// TODO Auto-generated method stub
		return this.inquilino == us;
	}

	public boolean fecahPosteriorA(LocalDate now) {
		// TODO Auto-generated method stub
		return this.fechaInicio.isAfter(now);
	}

	public boolean esDeCiudad(String ciudad) {
		// TODO Auto-generated method stub
		return this.alquiler.esDeCiudad(ciudad);
	}

	public String getCiudad() {
		// TODO Auto-generated method stub
		return this.alquiler.getCiudad();
	}
}
