package sistema.alquiler.politicaDeCancelacion;

import java.time.LocalDate;

public class SinCancelacion extends PoliticaDeCancelacion {
	@Override
	public double calcularReembolso(LocalDate fechaInicio, LocalDate fechaFinal, double precioTotal) {
		return 0.0;
	}
}