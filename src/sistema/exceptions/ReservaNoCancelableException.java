package sistema.exceptions;

public class ReservaNoCancelableException extends Exception {

	public ReservaNoCancelableException() {
		super("No se puede cancelar la reserva");
	}
}
