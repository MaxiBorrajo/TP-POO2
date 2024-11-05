package sistema.exceptions;

public class ValoracionInvalidaException extends Exception {
	public ValoracionInvalidaException() {
		super("No tienes permitido valorar esta entidad");
	}
}
