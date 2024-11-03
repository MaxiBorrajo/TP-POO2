package sistema.usuario;

import sistema.enums.RolDeUsuario;

public class Inquilino extends Usuario {
	
	
	public Inquilino(String nombre, String mail , String telefono) {
		super(nombre,mail, telefono);
		this.rol = RolDeUsuario.INQUILINO;
	}
}
