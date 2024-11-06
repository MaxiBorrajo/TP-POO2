package sistema.alquiler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import sistema.reserva.Reserva;
import sistema.reserva.politicaDeCancelacion.PoliticaDeCancelacion;
import sistema.Inmueble.Inmueble;
import sistema.enums.FormaDePago;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.filtro.FiltroSimple;
import sistema.managers.ReservaManager;
import sistema.periodo.Periodo;

public class Alquiler {
	private Inmueble inmueble;
	private LocalTime checkIn;
	private LocalTime checkOut;
	private List<FormaDePago> formasDePago;
	private List<Periodo> periodos;
	private List<Periodo> diasNoDisponibles;
	private double precioDefault;
	private Queue<Reserva> reservasEncoladas;
	private PoliticaDeCancelacion politicaDeCancelacion;

	public Alquiler(Inmueble inmueble, LocalTime checkIn, LocalTime checkOut, double precioDefault, PoliticaDeCancelacion politicaDeCancelacion) {
		this.inmueble = inmueble;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.precioDefault = precioDefault;
		this.formasDePago = new ArrayList<>();
		this.periodos = new ArrayList<>();
		this.diasNoDisponibles = new ArrayList<>();
		this.reservasEncoladas = new LinkedList<>();
		this.politicaDeCancelacion = politicaDeCancelacion;
	}

	public void agregarPeriodo(Periodo periodo) {
		// validar si no existe un periodo que coincida con este, no puede haber
		// mas de un precio por dia
		this.periodos.add(periodo);
	}
	
	private void agregarPeriodoNoDisponible(Periodo p) {
		// TODO Auto-generated method stub
		this.diasNoDisponibles.add(p);
	}

	public double calcularPrecioPeriodo(LocalDate fechaInicio, LocalDate fechaFinal) {
		// this.periodos.strea().mapToInt(p -> p.valorParaPeriodo(fechaInicio,
		// fechaFinal)).size() -
		// - calcularDistanciaFecha(fechaInicio, fechaFinal)) modulo *precioBase + lo
		// anterior
		// this.periodos.stream().reduce(((acc, p ) -> acc + p.isEmpty() ? p.get() : 0)
		// ,0)
		//
		double totalPrecio = 0.0;
		LocalDate currentDate = fechaInicio;
		while (!currentDate.isAfter(fechaFinal)) {
			boolean precioAsignado = false;
			for (Periodo periodo : periodos) {
				if (periodo.perteneceAPeriodo(currentDate)) {
					totalPrecio += periodo.getPrecio();
					precioAsignado = true;
					break;
				}
			}
			if (!precioAsignado) {
				totalPrecio += precioDefault;
			}
			currentDate = currentDate.plusDays(1);
		}
		return totalPrecio;
	}

	public boolean puedeCrearReserva(LocalDate entrada, LocalDate salida) {
		LocalDate currentDate = entrada;

		while (!currentDate.isAfter(salida)) {
			for (Periodo periodo : diasNoDisponibles) {
				if (periodo.perteneceAPeriodo(currentDate)) {
					return false;
				}
			}
			currentDate = currentDate.plusDays(1);
		}
		return true;
	}

	public boolean validateFormaDePago(FormaDePago formaDePago) {
		return this.formasDePago.contains(formaDePago);
	}

	public void agregarFormaDePago(FormaDePago formaDePago) {
		if (!validateFormaDePago(formaDePago)) {
			this.formasDePago.add(formaDePago);
		}
	}

	public boolean esDeCiudad(String ciudad) {
		// TODO Auto-generated method stub
		return this.inmueble.esDeCiudad(ciudad);
	}

	public boolean perteneceAAlgunPeriodo(LocalDate entrada, LocalDate salida) {
		// TODO Auto-generated method stub ver si rompe con algun caso, esto
		// posiblemente ya no se usa
		return this.periodos.stream().anyMatch(p -> p.perteneceAPeriodo(entrada))
				&& this.periodos.stream().anyMatch(p -> p.perteneceAPeriodo(salida));
	}

	public boolean aceptaCantidadHuespedes(int cant) {
		// TODO Auto-generated method stub
		return cant >= this.inmueble.getCapacidad();
	}

	public boolean cumplePrecioEnPeriodo(double precioMinimo, double precioMaximo, LocalDate entrada,
			LocalDate salida) {
		// TODO Auto-generated method stub
		double precioPorFecha = this.calcularPrecioPeriodo(entrada, salida);
		return precioMinimo <= precioPorFecha & precioPorFecha >= precioMaximo;
	}

	public Inmueble getInmueble() {
		return this.inmueble;
	}

	public boolean estaDisponibleLuego(LocalDate entrada) {
		// TODO Auto-generated method stub
		return this.periodos.stream().anyMatch(p -> p.perteneceAPeriodo(entrada));
	}

	public boolean estaDisponibleAntes(LocalDate salida) {
		// TODO Auto-generated method stub
		return this.periodos.stream().anyMatch(p -> p.perteneceAPeriodo(salida));
	}

	public String getTipoDeInmueble() {
		// TODO Auto-generated method stub
		return this.inmueble.getTipo();
	}

	public boolean tienenElMismoInmueble(Alquiler alq) {
		// TODO Auto-generated method stub
		// por precondicion de la solucion se podria compara por alquileres
		return this.inmueble.sonElMismoInmueble(alq.getInmueble());
	}

	public double getPrecioBase() {
		// TODO Auto-generated method stub
		return this.precioDefault;
	}

	public String getCiudad() {
		// TODO Auto-generated method stub
		return this.inmueble.getCiudad();
	}

	public void encolarReserva(Reserva nuevaReserva) {
		this.reservasEncoladas.add(nuevaReserva);
	}
	
	public Reserva obtenerPrimeroDeReservasEncoladas() {
		return this.reservasEncoladas.poll();
	}

	public boolean hayReservasEncoladas() {
		// TODO Auto-generated method stub
		return !this.reservasEncoladas.isEmpty();
	}

	

	public void seAceptoReserva(Reserva reserva) {
		// TODO Auto-generated method stub
		//ocupar periodos
		LocalDate fechaInicio = reserva.getFechaInicio();
		LocalDate fechaFinal = reserva.getFechaFinal();
		//this.inmueble.setVecesAlquilado(this.inmueble.getVecesAlquilado() + 1); deberia ir aca ya que recien aca en teoria se acepta
		this.agregarPeriodoNoDisponible(new Periodo(fechaInicio, fechaFinal ,this.calcularPrecioPeriodo(fechaInicio, fechaFinal)));
	}

	

	public void seCanceloReserva(Reserva reserva, ReservaManager reser) throws AlquilerNoDisponibleException, FormaDePagoNoAceptadaException {
		// TODO Auto-generated method stub
		//estrategia de cancelacion
		//ver si hay que desencolar
		if(this.hayReservasEncoladas()) {
			reser.desencolarReserva(this);
		}else {
			this.desocuparPeriodosDe(reserva.getFechaInicio(), reserva.getFechaFinal());
		}
	}

	private void desocuparPeriodosDe(LocalDate fechaInicio, LocalDate fechaFinal) {
		// TODO Auto-generated method stub
		Periodo pe = (new FiltroSimple<Periodo>(p -> p.peridoDeFecha(fechaInicio , fechaFinal)))
				.filtrarLista(this.diasNoDisponibles).stream().findFirst().get();
		this.diasNoDisponibles.remove(pe);
		this.agregarPeriodo(pe);
	}


	public double calcularReembolsoPorCancelacion(LocalDate fechaInicio, LocalDate fechaFinal, double precioTotal) {
		return this.politicaDeCancelacion.calcularReembolso(fechaInicio, fechaFinal, precioTotal);
	}

}
