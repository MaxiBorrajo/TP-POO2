package sistema.usuario;

import sistema.Inmueble.Inmueble;
import sistema.enums.RolDeUsuario;
import sistema.exceptions.InmuebleYaGuardadoException;
import sistema.exceptions.UsuarioExistenteException;

import java.util.ArrayList;
import java.util.List;

public class Propietario extends Usuario{
	private List<Inmueble> inmuebles;
	
	public Propietario(String nombreCompleto, String email, String telefono) {
		super(nombreCompleto, email, telefono);
		this.inmuebles = new ArrayList<>();
		this.rol = RolDeUsuario.PROPIETARIO;
	}

	public boolean tieneInmueble(Inmueble inmueble) {
		return this.inmuebles.contains(inmueble);
	}
	public List<Inmueble> getInmuebles() {
		return this.inmuebles;
	}
	
	public void addInmueble(Inmueble inmueble) throws InmuebleYaGuardadoException {
		if (this.inmuebles.contains(inmueble)) {
			throw new InmuebleYaGuardadoException("Este inmueble ya fue asociado");
		} else {
			this.inmuebles.add(inmueble);
		}
	}
	

}
