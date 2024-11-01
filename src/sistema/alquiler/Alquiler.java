package sistema.alquiler;
import java.time.LocalDate;
import java.util.List;
import sistema.reserva.Reserva;
import sistema.Inmueble.Inmueble;
import sistema.enums.FormaDePago;
import sistema.periodo.Periodo;
public class Alquiler {
	private int id;
	private Inmueble inmueble;
	private String checkIn;
	private String checkOut;
	private List<FormaDePago> formasDePago;
	private List<Periodo> periodos;
	private List<Periodo> diasRervados;
	private double precioDefault;
	
	
	
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	public void agregarPeriodo(Periodo periodo) {
		//validar si no existe un periodo que coincida con este, no puede haber
		//mas de un precio por dia
		this.periodos.add(periodo);
	}
	public double cacularPrecioPeriodo(LocalDate fechaInicio, LocalDate fechaFinal) {
		//this.periodos.strea().mapToInt(p -> p.valorParaPeriodo(fechaInicio, fechaFinal)).size() -
		//-  calcularDistanciaFecha(fechaInicio, fechaFinal)) modulo  *precioBase + lo anterior   
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
	
	
	public boolean esDeCiudad(String ciudad) {
		// TODO Auto-generated method stub
		return this.inmueble.esDeCiudad(ciudad);
	}
	
	public boolean perteneceAAlgunPeriodo(LocalDate entrada, LocalDate salida) {
		// TODO Auto-generated method stub ver si rompe con algun caso
		return this.periodos.stream().anyMatch(p -> p.perteneceAPeriodo(entrada)) && this.periodos.stream().anyMatch(p -> p.perteneceAPeriodo(salida));
	}
	public boolean aceptaCantidadHuespedes(int cant) {
		// TODO Auto-generated method stub
		return cant >= this.inmueble.getCapacidad();
	}
	
	public boolean cumplePrecioEnPeriodo(double precioMinimo, double precioMaximo, LocalDate entrada, LocalDate salida) {
		// TODO Auto-generated method stub
		double precioPorFecha = this.cacularPrecioPeriodo(entrada, salida);
		return precioMinimo <= precioPorFecha & precioPorFecha >= precioMaximo;
	}
	
}
