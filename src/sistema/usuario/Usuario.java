package sistema.usuario;

import sistema.enums.RolDeUsuario;

public class Usuario {
	private String nombre;
	private String email;
	private String telefono;
	protected RolDeUsuario rol;


	public Usuario(String nombreCompleto, String email, String telefono) {
		this.nombre = nombreCompleto;
		this.email = email;
		this.telefono = telefono;
	}

	public String getNombre() {
		return this.nombre;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Usuario))
			return false;
		Usuario other = (Usuario) obj;
		return email.equals(other.email);
	}
	
	public String getEmail() {
		return this.email;
	}
	public String getTelefono() {
		return this.telefono;
	}
	public RolDeUsuario getRol() {
		return this.rol;
	}

}
