package sistema.Usuario;

import sistema.Inmueble.Inmueble;

import java.util.List;

public class Propietario extends Usuario{
	private List<Inmueble> inmuebles;
	
	public Propietario(String nombreCompleto, String email, String telefono) {
		super(nombreCompleto, email, telefono);
	}

	
	public List<Inmueble> getinmueble() {
		return this.inmuebles;
	}
	
	public void addInmueble(Inmueble inmueble) {
		this.inmuebles.add(inmueble);
	}

}
