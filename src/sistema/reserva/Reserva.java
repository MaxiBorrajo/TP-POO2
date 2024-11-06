package sistema.reserva;

import java.time.LocalDate;

import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;
import sistema.enums.*;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.ReservaNoCancelableException;
import sistema.managers.NotificadorManager;
import sistema.managers.ReservaManager;
import sistema.notificaciones.CancelacionNotify;
import sistema.notificaciones.ReservaNotify;
import sistema.usuario.Inquilino;
import sistema.usuario.Usuario;

public class Reserva {
	private EstadoReserva estado;
	private FormaDePago formaDepago;
	private LocalDate fechaInicio;
	private LocalDate fechaFinal;
	private Alquiler alquiler;
	private Inquilino inquilino;
	private NotificadorManager noti;
	private double precioTotal;

	public EstadoReserva getEstado() {
		return estado;
	}

	public Reserva(FormaDePago formaDepago, LocalDate fechaInicio, LocalDate fechaFinal, Alquiler alquiler,
			Usuario usuario, double precioTotal) {
		this.estado = new Pendiente();
		this.formaDepago = formaDepago;
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		this.alquiler = alquiler;
		this.inquilino = usuario;
		this.precioTotal = precioTotal;

		Inmueble inmuebleReservado = this.alquiler.getInmueble();
		inmuebleReservado.setVecesAlquilado(inmuebleReservado.getVecesAlquilado() + 1);
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public FormaDePago getFormaDepago() {
		return formaDepago;
	}

	public LocalDate getFechaFinal() {
		return fechaFinal;
	}

	public Inquilino getInquilino() {
		return this.inquilino;
	}

	public boolean esDeUsuario(Usuario us) {
		// TODO Auto-generated method stub
		return this.inquilino == us;
	}

	public boolean fechaPosteriorAInicio(LocalDate date) {
		// TODO Auto-generated method stub
		return this.fechaInicio.isAfter(date);
	}

	public boolean fechaPosteriorAFinal(LocalDate date) {
		// TODO Auto-generated method stub
		return date.isAfter(fechaFinal);
	}

	public boolean esDeCiudad(String ciudad) {
		// TODO Auto-generated method stub
		return this.alquiler.esDeCiudad(ciudad);
	}

	public Alquiler getAlquiler() {
		return alquiler;
	}

	public String getCiudad() {
		// TODO Auto-generated method stub
		return this.alquiler.getCiudad();
	}

	public void aceptar(NotificadorManager noti) {
		this.estado.aceptar(this, noti);

	}

	public void rechazar() {
		this.estado.rechazar(this);
	}

	public void cancelar(ReservaManager reservaManager, NotificadorManager notificadorManager)
			throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException, ReservaNoCancelableException {
		this.estado.cancelar(this, reservaManager, notificadorManager);

	}

	public void setEstado(EstadoReserva estado) {
		this.estado = estado;
	}

	public void finalizar() {
		this.estado.finalizar(this);
		;
	}

	public void aceptarReserva(NotificadorManager noti2) {
		// TODO Auto-generated method stub
		noti2.notify(new ReservaNotify(this.alquiler));
		this.setEstado(new Aceptada());
		;
		this.alquiler.seAceptoReserva(this);
	}

	public void cancelarReserva(ReservaManager reser, NotificadorManager noti2)
			throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException, ReservaNoCancelableException {
		// TODO Auto-generated method stub
		noti2.notify(new CancelacionNotify(this.alquiler));
		this.setEstado(new Cancelada());
		this.alquiler.seCanceloReserva(this, reser);

	}

	public double calcularReembolsoPorCancelacion() {
		return this.alquiler.calcularReembolsoPorCancelacion(this.fechaInicio, this.fechaFinal, this.precioTotal);
	}

	public boolean estaActiva() {
		// TODO Auto-generated method stub
		return this.estado.estaAceptada();
	}
	// r.getEstado() != EstadoDeReserva.CANCELADA && r.getEstado() !=
	// EstadoDeReserva.FINALIZADA

}
