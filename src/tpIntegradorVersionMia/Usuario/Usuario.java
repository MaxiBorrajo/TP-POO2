package tpIntegradorVersionMia.Usuario;

public abstract class Usuario {
	private String nombre;
	private String email;
	private String telefono;
	
	
	public Usuario(String nombreCompleto, String email, String telefono) {
		this.nombre = nombreCompleto;
		this.email = email;
		this.telefono = telefono;
	}
	
	
}
