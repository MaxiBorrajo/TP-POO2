package sistema.reserva;
import java.time.LocalDate;

import sistema.alquiler.Alquiler;
import sistema.enums.*;
public class Reserva {
	private EstadoDeReserva estado;
	private FormaDePago formaDepago;
	private LocalDate fechaInicio;
	private LocalDate fechaFinal;
	private Alquiler alquiler;
}
