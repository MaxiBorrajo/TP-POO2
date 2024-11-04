package sistema.usuario;

import sistema.enums.RolDeUsuario;

public class Inquilino extends Usuario {
	private RolDeUsuario rol;
	
	public Inquilino(String nombre, String mail , String telefono) {
		super(nombre,mail, telefono);
		this.rol = RolDeUsuario.INQUILINO;
	}
}
