package sistema.exceptions;

public class CustomEnumExistenteException extends Exception {
	public CustomEnumExistenteException() {
		super("Custon enum ya existe");
	}

}
