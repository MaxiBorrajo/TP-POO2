package sistema.exceptions;

public class UsuarioExistenteException extends Exception {

	public UsuarioExistenteException() {
		super("El usuario ya existe");
	}

}
