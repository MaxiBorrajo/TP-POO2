package sistema.exceptions;

public class TotalFotosExcedidoException extends Exception {

	public TotalFotosExcedidoException() {
		super("No puedes agregar más de cinco fotos");
	}

}
