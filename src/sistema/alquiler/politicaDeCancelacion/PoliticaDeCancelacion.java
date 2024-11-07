package sistema.alquiler.politicaDeCancelacion;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class PoliticaDeCancelacion {
	protected int diasAlComienzo(LocalDate startDate) {
		return (int) ChronoUnit.DAYS.between(LocalDate.now(), startDate);
	}

	public abstract double calcularReembolso(LocalDate fechaInicio, LocalDate fechaFinal, double precioTotal);
}