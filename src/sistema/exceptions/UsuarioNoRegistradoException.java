package sistema.exceptions;

public class UsuarioNoRegistradoException extends Exception {
	public UsuarioNoRegistradoException() {
		super("El usuario dado no esta registrado");
	}
}
