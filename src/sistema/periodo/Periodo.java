package sistema.periodo;

import java.time.LocalDate;

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
        return (fecha != null && 
                (fecha.isEqual(fechaInicio) || fecha.isAfter(fechaInicio)) &&
                (fecha.isEqual(fechaFinal) || fecha.isBefore(fechaFinal)));
    }
	
	public double getPrecio() {
		return this.precio;
		
	}
	
}
