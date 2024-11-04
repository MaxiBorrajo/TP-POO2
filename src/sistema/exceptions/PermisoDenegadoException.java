package sistema.exceptions;

public class PermisoDenegadoException extends Exception {
	public PermisoDenegadoException() {
		super("El usuario no cuenta con permisos para realizar esta acción");
	}
}
