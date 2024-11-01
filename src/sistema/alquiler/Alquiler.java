package sistema.alquiler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import sistema.reserva.Reserva;
import sistema.Inmueble.Inmueble;
import sistema.enums.FormaDePago;
import sistema.exceptions.AlquilerNoDisponibleException;
import sistema.exceptions.FormaDePagoNoAceptadaException;
import sistema.periodo.Periodo;

public class Alquiler {
	private int id;
	private Inmueble inmueble;
	private LocalTime checkIn;
	private LocalTime checkOut;
	private List<FormaDePago> formasDePago;
	private List<Periodo> periodos;
	private List<Periodo> diasNoDisponibles;
	private double precioDefault;

	public Alquiler(Inmueble inmueble, LocalTime checkIn, LocalTime checkOut, double precioDefault, int id) {
		this.id = id;
		this.inmueble = inmueble;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.precioDefault = precioDefault;
		this.formasDePago = new ArrayList<>();
		this.periodos = new ArrayList<>();
		this.diasNoDisponibles = new ArrayList<>();
	}

	public int getId() {
		return this.id;
	}

	public void agregarPeriodo(Periodo periodo) {
		// validar si no existe un periodo que coincida con este, no puede haber
		// mas de un precio por dia
		this.periodos.add(periodo);
	}

	public double cacularPrecioPeriodo(LocalDate fechaInicio, LocalDate fechaFinal) {
		// this.periodos.strea().mapToInt(p -> p.valorParaPeriodo(fechaInicio,
		// fechaFinal)).size() -
		// - calcularDistanciaFecha(fechaInicio, fechaFinal)) modulo *precioBase + lo
		// anterior
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


	public boolean esDeCiudad(String ciudad) {
		// TODO Auto-generated method stub
		return this.inmueble.esDeCiudad(ciudad);
	}

	public boolean perteneceAAlgunPeriodo(LocalDate entrada, LocalDate salida) {
		// TODO Auto-generated method stub ver si rompe con algun caso
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
		double precioPorFecha = this.cacularPrecioPeriodo(entrada, salida);
		return precioMinimo <= precioPorFecha & precioPorFecha >= precioMaximo;
	}

}
