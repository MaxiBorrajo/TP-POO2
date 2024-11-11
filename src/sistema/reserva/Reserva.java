package sistema.reserva;

import java.time.LocalDate;

import sistema.alquiler.Alquiler;
import sistema.enums.*;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.ReservaNoCancelableException;
import sistema.managers.NotificadorManager;
import sistema.managers.ReservaManager;
import sistema.notificaciones.CancelacionNotify;
import sistema.notificaciones.Observable;
import sistema.notificaciones.ReservaNotify;
import sistema.usuario.Usuario;

public class Reserva implements Observable {
	private EstadoReserva estado;
	private FormaDePago formaDepago;
	private LocalDate fechaInicio;
	private LocalDate fechaFinal;
	private Alquiler alquiler;
	private Usuario inquilino;
	private NotificadorManager noti;
	private double precioTotal;

	

	public Reserva(FormaDePago formaDepago, LocalDate fechaInicio, LocalDate fechaFinal, Alquiler alquiler,
			Usuario usuario, double precioTotal) {
		this.estado = new Pendiente();
		this.formaDepago = formaDepago;
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		this.alquiler = alquiler;
		this.inquilino = usuario;
		this.precioTotal = precioTotal;
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

	public Usuario getInquilino() {
		return this.inquilino;
	}

	public boolean esDeUsuario(Usuario us) {
		// TODO Auto-generated method stub
		return this.inquilino == us;
	}

	public boolean fechaPosteriorAInicio(LocalDate date) {
		// TODO Auto-generated method stub
		return this.fechaInicio.isBefore(date);
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
		
	}

	public void aceptarReserva(NotificadorManager noti2) {
		// TODO Auto-generated method stub
		noti2.notify(new ReservaNotify(this));
		this.setEstado(new Aceptada());
		;
		this.alquiler.seAceptoReserva(this);
	}

	public void cancelarReserva(ReservaManager reser, NotificadorManager noti2)
			throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException, ReservaNoCancelableException {

		noti2.notify(new CancelacionNotify(this));
		this.setEstado(new Cancelada());
		this.alquiler.seCanceloReserva(this, reser);

	}
	
	public double calcularReembolsoPorCancelacion() {
		return this.alquiler.calcularReembolsoPorCancelacion(this.fechaInicio, this.fechaFinal, this.precioTotal);
	}

	public boolean estaActiva() {
		return this.estado.estaAceptada();
	}

	public boolean estaRechazada() {
		// TODO Auto-generated method stub
		return this.estado.estaRechazada();
	}

	public boolean estaFinalizada() {
		// TODO Auto-generated method stub
		return this.estado.estaFinalizada();
	}

	


}
