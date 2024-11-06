package sistema.reserva.politicaDeCancelacion;

import java.time.LocalDate;

public class CancelacionIntermedia extends PoliticaDeCancelacion {
	@Override
	public double calcularReembolso(LocalDate fechaInicio, LocalDate fechaFinal, double precioTotal) {
		int daysUntilStart = diasAlComienzo(fechaInicio);
		if (daysUntilStart > 20) {
			return precioTotal;
		} else if (daysUntilStart >= 10) {
			return precioTotal * 0.5;
		} else {
			return 0.0;
		}
	}
}