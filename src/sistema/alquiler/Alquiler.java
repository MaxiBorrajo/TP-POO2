package sistema.alquiler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import sistema.reserva.Reserva;
import sistema.usuario.Usuario;
import sistema.Inmueble.Inmueble;
import sistema.alquiler.politicaDeCancelacion.PoliticaDeCancelacion;
import sistema.enums.FormaDePago;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.exceptions.YaExistenteException;
import sistema.filtro.FiltroSimple;
import sistema.managers.NotificadorManager;
import sistema.managers.ReservaManager;
import sistema.notificaciones.BajaPrecioNotify;
import sistema.notificaciones.Observable;
import sistema.periodo.Periodo;

public class Alquiler implements Observable {

	private Inmueble inmueble;
	private LocalTime checkIn;
	private LocalTime checkOut;
	private List<FormaDePago> formasDePago;
	private List<Periodo> periodos;
	private List<Periodo> diasNoDisponibles;
	private double precioDefault;
	private Queue<Reserva> reservasEncoladas;
	private PoliticaDeCancelacion politicaDeCancelacion;

	public Alquiler(Inmueble inmueble, LocalTime checkIn, LocalTime checkOut, double precioDefault,
			PoliticaDeCancelacion politicaDeCancelacion, List<FormaDePago> formasDePago) {
		this.inmueble = inmueble;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.precioDefault = precioDefault;
		this.formasDePago = formasDePago;
		this.periodos = new ArrayList<>();
		this.diasNoDisponibles = new ArrayList<>();
		this.reservasEncoladas = new LinkedList<>();
		this.politicaDeCancelacion = politicaDeCancelacion;
	}

	public void setPoliticaDeCancelacion(PoliticaDeCancelacion politicaDeCancelacion) {
		this.politicaDeCancelacion = politicaDeCancelacion;
	}
	public PoliticaDeCancelacion getPoliticaDeCancelacion() {
		return this.politicaDeCancelacion;
	}
	public void agregarPeriodo(Periodo nuevoPeriodo) {
		boolean existePeriodo = this.periodos.stream()
				.anyMatch(periodo -> (nuevoPeriodo.perteneceAPeriodo(periodo.getFechaInicio())
						|| nuevoPeriodo.perteneceAPeriodo(periodo.getFechaFinal()))
						|| (periodo.perteneceAPeriodo(nuevoPeriodo.getFechaInicio())
								|| periodo.perteneceAPeriodo(nuevoPeriodo.getFechaFinal())));

		if (!existePeriodo) {
			this.periodos.add(nuevoPeriodo);
		} else {
			throw new IllegalArgumentException(
					"El periodo ya existe o se solapa con otro y no puede ser agregado nuevamente.");
		}
	}

	private void agregarPeriodoNoDisponible(Periodo p) {
		this.diasNoDisponibles.add(p);
	}

	public double calcularPrecioPeriodo(LocalDate fechaInicio, LocalDate fechaFinal) {
		return fechaInicio.datesUntil(fechaFinal.plusDays(1))
				.mapToDouble(date -> periodos.stream().filter(periodo -> periodo.perteneceAPeriodo(date)).findFirst()
						.map(Periodo::getPrecio).orElse(precioDefault))
				.sum();
	}

	public boolean puedeCrearReserva(LocalDate entrada, LocalDate salida) {
		return entrada.datesUntil(salida.plusDays(1))
				.noneMatch(date -> diasNoDisponibles.stream().anyMatch(periodo -> periodo.perteneceAPeriodo(date)));
	}

	public boolean existeFormaDePago(FormaDePago formaDePago) {
		return this.formasDePago.contains(formaDePago);
	}

	public void agregarFormaDePago(FormaDePago formaDePago) throws YaExistenteException {
		if (!existeFormaDePago(formaDePago)) {
			this.formasDePago.add(formaDePago);
		} else {
			throw new YaExistenteException("Forma de pago");
		}
	}

	public boolean esDeCiudad(String ciudad) {
		return this.inmueble.esDeCiudad(ciudad);
	}

	public boolean aceptaCantidadHuespedes(int cant) {
		return cant >= this.inmueble.getCapacidad();
	}

	public boolean cumplePrecioEnPeriodo(double precioMinimo, double precioMaximo, LocalDate entrada,
			LocalDate salida) {
		double precioPorFecha = this.calcularPrecioPeriodo(entrada, salida);
		return precioMinimo <= precioPorFecha & precioPorFecha >= precioMaximo;
	}

	public Inmueble getInmueble() {
		return this.inmueble;
	}

	

	public String getTipoDeInmueble() {
		return this.inmueble.getTipo();
	}

	public boolean tienenElMismoInmueble(Alquiler alq) {
		return this.inmueble.sonElMismoInmueble(alq.getInmueble());
	}

	public double getPrecioBase() {
		return this.precioDefault;
	}

	public String getCiudad() {
		return this.inmueble.getCiudad();
	}

	public void encolarReserva(Reserva nuevaReserva) {
		this.reservasEncoladas.add(nuevaReserva);
	}

	public Reserva obtenerPrimeroDeReservasEncoladas() {
		return this.reservasEncoladas.poll();
	}

	public boolean hayReservasEncoladas() {
		return !this.reservasEncoladas.isEmpty();
	}

	public void seAceptoReserva(Reserva reserva) {
		LocalDate fechaInicio = reserva.getFechaInicio();
		LocalDate fechaFinal = reserva.getFechaFinal();
		this.inmueble.setVecesAlquilado(this.inmueble.getVecesAlquilado() + 1);
		this.agregarPeriodoNoDisponible(
				new Periodo(fechaInicio, fechaFinal, this.calcularPrecioPeriodo(fechaInicio, fechaFinal)));
	}

	public void seCanceloReserva(Reserva reserva, ReservaManager reser)
			throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException {
		this.desocuparPeriodosDe(reserva.getFechaInicio(), reserva.getFechaFinal());
		
		if (this.hayReservasEncoladas()) {
			reser.desencolarReserva(this);
		}
	}

	private void desocuparPeriodosDe(LocalDate fechaInicio, LocalDate fechaFinal) {
		FiltroSimple<Periodo> filtro = new FiltroSimple<>(p -> p.periodoDeFecha(fechaInicio, fechaFinal));
		Optional<Periodo> periodoOptional = filtro.filtrarLista(this.diasNoDisponibles).stream().findFirst();

		if (periodoOptional.isPresent()) {
			Periodo pe = periodoOptional.get();
			this.diasNoDisponibles.remove(pe);
			this.agregarPeriodo(pe);
		}
	}

	public double calcularReembolsoPorCancelacion(LocalDate fechaInicio, LocalDate fechaFinal, double precioTotal) {
		return this.politicaDeCancelacion.calcularReembolso(fechaInicio, fechaFinal, precioTotal);
	}

	@Override
	public Alquiler getAlquiler() {
		return this;
	}

	public Usuario getPropietario() {
		// TODO Auto-generated method stub
		return this.inmueble.getPropietario();
	}

	public void cambiarPrecio(double precio, NotificadorManager notificadorManager) {
		if(getPrecioBase() > precio) {
			notificadorManager.notify(new BajaPrecioNotify(this));
		}
		
		this.precioDefault = precio;
	}

}
