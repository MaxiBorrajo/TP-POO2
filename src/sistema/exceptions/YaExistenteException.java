package sistema.exceptions;

public class YaExistenteException extends Exception {
	public YaExistenteException(String entidad) {
		super(entidad + " ya existe");
	}
}
