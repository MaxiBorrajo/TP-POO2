package sistema.periodo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class Periodo {
	private LocalDate fechaInicio;
	private LocalDate fechaFinal;
	private double precio;
	
	public Periodo(LocalDate fechaInicio, LocalDate fechaFinal, double precio){
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		this.precio = precio; 
	}

    public boolean perteneceAPeriodo(LocalDate fecha) {
        return  (fecha.isEqual(fechaInicio) || fecha.isAfter(fechaInicio)) &&
                (fecha.isEqual(fechaFinal) || fecha.isBefore(fechaFinal));
    }
	
	public double getPrecio() {
		return this.precio;
		
	}
	
	public boolean precioMinimoEsMenorA(double precioMinimo) {
		return precioMinimo >= this.precio;
	}

	public boolean precioMayorA(double precioMaximo) {
		return precioMaximo <=this.precio;
	}

	public boolean peridoDeFecha(LocalDate fechaInicio2, LocalDate fechaFinal2) {
		return this.fechaInicio.equals(fechaInicio2) & this.fechaFinal.equals(fechaFinal2);
	}
	

}
