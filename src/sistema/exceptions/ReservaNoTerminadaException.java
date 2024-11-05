package sistema.exceptions;

public class ReservaNoTerminadaException extends Exception {
	public ReservaNoTerminadaException() {
		super("La reserva aun no ha terminado");
	}
}
