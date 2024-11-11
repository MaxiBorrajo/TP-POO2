package sistema.exceptions;

public class ServicioNoTerminadoException extends Exception {
	public ServicioNoTerminadoException() {
		super("El servicio aun no ha finalizado.");
	}
}
