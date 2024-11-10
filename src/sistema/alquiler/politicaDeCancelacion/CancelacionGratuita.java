package sistema.alquiler.politicaDeCancelacion;

import java.time.LocalDate;

public class CancelacionGratuita extends PoliticaDeCancelacion {
	@Override
	public double calcularReembolso(LocalDate fechaInicio, LocalDate fechaFinal, double precioTotal) {
		int diasAlComienzo = diasAlComienzo(fechaInicio);
		if (diasAlComienzo <= 0) {
			return 0;
		}
		if (diasAlComienzo > 10) { 
			return precioTotal;
		} else {
			int diasReservados = (int) (fechaFinal.toEpochDay() - fechaInicio.toEpochDay());
			return precioTotal - (2 * (precioTotal / diasReservados));
		}
	}
}