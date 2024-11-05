package sistema.exceptions;

public class RangoValoracionInvalidoException extends Exception {
	public RangoValoracionInvalidoException() {
		super("El rango de valoración permitido es de 1 a 5");
	}
}
