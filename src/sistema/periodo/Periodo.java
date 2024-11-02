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
	
	public Optional<Double> precioParaLasFechas(LocalDate entrada, LocalDate salida){
		double n = 0 ;
		if(this.perteneceAPeriodo(entrada) && this.perteneceAPeriodo(salida)) {
			 n  = ChronoUnit.DAYS.between(entrada, salida) * this.precio;
			 
		}else if(this.perteneceAPeriodo(entrada)) {
			 n  = ChronoUnit.DAYS.between(entrada, this.fechaFinal) * this.precio;
		}else if(this.perteneceAPeriodo(salida)) {
			 n  = ChronoUnit.DAYS.between(this.fechaInicio, salida) * this.precio;
		}
		
		
		
		return n== 0 ?  Optional.empty() :Optional.of(n);
			
	}

	public boolean precioMinimoEsMenorA(double precioMinimo) {
		// TODO Auto-generated method stub
		return precioMinimo >= this.precio;
	}

	public boolean precioMayorA(double precioMaximo) {
		// TODO Auto-generated method stub
		return precioMaximo <=this.precio;
	}
	

}
