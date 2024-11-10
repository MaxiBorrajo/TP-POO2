package sistema.exceptions;

public class CantidadFotosExcedidaException extends Exception {

	public CantidadFotosExcedidaException() {
		super("Solo puede añadir 5 fotos por inmueble");
	}

}
