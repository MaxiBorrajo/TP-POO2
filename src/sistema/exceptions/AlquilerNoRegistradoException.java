package sistema.exceptions;

public class AlquilerNoRegistradoException extends Exception {
	public AlquilerNoRegistradoException(){
		super("El Alquiler dado no esta registrado");
	}
}
