package sistema.exceptions;

public class NoExistenteException extends Exception {
	public NoExistenteException(String entidad) {
		super(entidad + " no encontrada");
	}
}
