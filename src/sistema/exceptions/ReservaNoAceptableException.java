package sistema.exceptions;

public class ReservaNoAceptableException extends Exception {
	public ReservaNoAceptableException() {
		super("No se puede aceptar la reserva");
	}
}
